package com.keepyuppy.KeepyUppy.post.service;

import com.keepyuppy.KeepyUppy.global.exception.CustomException;
import com.keepyuppy.KeepyUppy.global.exception.ExceptionType;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.communication.response.PostResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.post.domain.entity.Post;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementJPARepository;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementRepositoryImpl;
import com.keepyuppy.KeepyUppy.post.repository.PostJpaRepository;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementService {

    private final AnnouncementJPARepository announcementJPARepository;
    private final AnnouncementRepositoryImpl announcementRepository;
    private final PostJpaRepository postJpaRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public AnnouncementResponse createAnnouncement(
            CustomUserDetails userDetails,
            Long teamId,
            PostRequest request
    ){
        Member author = getMemberInTeam(userDetails.getUserId(), teamId);
        Team team = author.getTeam();
        // add author automatically to reader set
        Set<Member> readers = new HashSet<>(Collections.singletonList(author));

        Announcement announcement = Announcement.announcementBuilder()
                .title(request.getTitle())
                .author(author)
                .content(request.getContent())
                .type(ContentType.ANNOUNCEMENT)
                .team(team)
                .pinned(false)
                .readers(readers)
                .build();

        return AnnouncementResponse.of(announcementJPARepository.save(announcement));
    }

    public AnnouncementResponse viewAnnouncement(CustomUserDetails userDetails, Long announcementId){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        return AnnouncementResponse.of(announcement);
    }

    @Transactional
    public void deleteAnnouncement(CustomUserDetails userDetails, Long announcementId){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        Member author = announcement.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        announcementJPARepository.deleteById(announcementId);
    }

    @Transactional
    public AnnouncementResponse updateAnnouncement(
            CustomUserDetails userDetails,
            Long announcementId,
            PostRequest request
    ){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        Member author = announcement.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        announcement.update(request);
        return AnnouncementResponse.of(announcementJPARepository.save(announcement));
    }

    @Transactional
    public PostResponse convertAsPost(
            CustomUserDetails userDetails,
            Long announcementId,
            PostRequest request
    ){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        Member author = announcement.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        announcement.update(request);
        Post post = announcement.convertAsPost();

        announcementJPARepository.deleteById(announcementId);
        return PostResponse.of(postJpaRepository.save(post));
    }


    @Transactional
    public void pinAnnouncement(CustomUserDetails userDetails, Long announcementId, boolean pinned){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());

        if (member.getGrade() == Grade.TEAM_MEMBER){
            throw new CustomException(ExceptionType.ACTION_ACCESS_DENIED);
        }
        if (pinned && !checkPinnedNum(member.getTeam())){
            throw new IllegalArgumentException("공지는 3개까지만 고정할 수 있습니다.");
        }

        announcement.setPinned(pinned);
        announcementJPARepository.save(announcement);
    }

    public boolean checkPinnedNum(Team team){
        Set<Announcement> pinned = announcementJPARepository.findByTeamAndPinned(team, true);
        return pinned.size() < 3;
    }

    @Transactional
    public void markAsRead(CustomUserDetails userDetails, Long announcementId){
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ExceptionType.ANNOUNCEMENT_NOT_FOUND));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        announcement.addReader(member);
        announcementJPARepository.save(announcement);
    }

    public List<AnnouncementResponse> getUnreadAnnouncementsByUser(CustomUserDetails userDetails){
        List<Announcement> announcements = announcementRepository.findUnreadByUserId(userDetails.getUserId());
        return announcements.stream().map(AnnouncementResponse::of).toList();
    }

    public Page<AnnouncementResponse> getAnnouncementsByTeam(
            CustomUserDetails userDetails,
            Long teamId,
            int page
    ){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<Announcement> announcements = announcementRepository.findByTeamId(teamId, pageable);
        return announcements.map(AnnouncementResponse::of);
    }

    public List<AnnouncementResponse> getUnreadAnnouncementsByTeam(CustomUserDetails userDetails, Long teamId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        List<Announcement> announcements = announcementRepository.findUnreadByTeamId(userDetails.getUserId(), teamId);
        return announcements.stream().map(AnnouncementResponse::of).toList();
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new CustomException(ExceptionType.TEAM_ACCESS_DENIED));
    }

}
