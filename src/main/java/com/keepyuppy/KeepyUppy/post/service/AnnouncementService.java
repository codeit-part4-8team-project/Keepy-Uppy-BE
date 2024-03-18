package com.keepyuppy.KeepyUppy.post.service;

import com.keepyuppy.KeepyUppy.global.exception.AccessDeniedException;
import com.keepyuppy.KeepyUppy.global.exception.NotFoundException;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.repository.MemberRepositoryImpl;
import com.keepyuppy.KeepyUppy.post.communication.request.PostRequest;
import com.keepyuppy.KeepyUppy.post.communication.response.AnnouncementResponse;
import com.keepyuppy.KeepyUppy.post.domain.entity.Announcement;
import com.keepyuppy.KeepyUppy.post.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementJPARepository;
import com.keepyuppy.KeepyUppy.post.repository.AnnouncementRepositoryImpl;
import com.keepyuppy.KeepyUppy.security.jwt.CustomUserDetails;
import com.keepyuppy.KeepyUppy.team.domain.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementJPARepository announcementJPARepository;
    private final AnnouncementRepositoryImpl announcementRepository;
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

    public AnnouncementResponse viewAnnouncement(CustomUserDetails userDetails, Long teamId, Long announcementId){
        getMemberInTeam(userDetails.getUserId(), teamId);
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지글입니다."));
        return AnnouncementResponse.of(announcement);
    }

    @Transactional
    public void deleteAnnouncement(CustomUserDetails userDetails, Long teamId, Long announcementId){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지글입니다."));
        Member author = announcement.getAuthor();

        if (member.getGrade() == Grade.TEAM_MEMBER && !Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException("삭제할 권한이 없는 공지글입니다.");
        }
        announcementJPARepository.deleteById(announcementId);
    }

    @Transactional
    public AnnouncementResponse updateAnnouncement(
            CustomUserDetails userDetails,
            Long teamId,
            Long announcementId,
            PostRequest request
    ){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지글입니다."));
        Member author = announcement.getAuthor();

        if (!Objects.equals(member.getId(), author.getId())){
            throw new AccessDeniedException("수정할 권한이 없는 공지글입니다.");
        }
        announcement.update(request);
        return AnnouncementResponse.of(announcementJPARepository.save(announcement));
    }

    @Transactional
    public void pinAnnouncement(CustomUserDetails userDetails, Long teamId, Long announcementId, boolean pinned){
        Member member = getMemberInTeam(userDetails.getUserId(), teamId);
        Announcement announcement = announcementJPARepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지글입니다."));

        if (member.getGrade() == Grade.TEAM_MEMBER){
            throw new AccessDeniedException("공지를 고정하거나 해제할 수 있는 권한이 없습니다.");
        }
        if (pinned && !checkPinnedNum(member.getTeam())){
            throw new IllegalArgumentException("공지를 3개 이상 고정할 수 없습니다.");
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
                .orElseThrow(() -> new NotFoundException("존재하지 않는 공지글입니다."));
        Member member = getMemberInTeam(userDetails.getUserId(), announcement.getTeam().getId());
        announcement.addReader(member);
        announcementJPARepository.save(announcement);
    }

    public List<AnnouncementResponse> getUnreadAnnouncements(CustomUserDetails userDetails){
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
        Page<Announcement> announcements = announcementRepository.findByTeam(teamId, pageable);
        return announcements.map(AnnouncementResponse::of);
    }

    public Member getMemberInTeam(Long userId, Long teamId){
        return memberRepository.findMemberInTeamByUserId(userId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 속하지 않은 팀입니다."));
    }

}
