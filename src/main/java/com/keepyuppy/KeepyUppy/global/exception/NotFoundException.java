package com.keepyuppy.KeepyUppy.global.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends NotFoundException{
        public UserNotFoundException() {
            super("유저를 찾을 수 없습니다.");
        }
    }

    public static class MemberNotFoundException extends NotFoundException {
        public MemberNotFoundException() {
            super("멤버를 찾을 수 없습니다.");
        }
    }

    public static class TeamNotFoundException extends RuntimeException {
        public TeamNotFoundException() {
            super("팀을 찾을 수 없습니다.");
        }
    }

    public static class IssueNotFoundException extends RuntimeException {
        public IssueNotFoundException() {
            super("이슈를 찾을 수 없습니다.");
        }
    }

    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException() {
            super("게시글을 찾을 수 없습니다.");
        }
    }

    public static class AnnouncementNotFoundException extends RuntimeException {
        public AnnouncementNotFoundException() {
            super("공지글을 찾을 수 없습니다.");
        }
    }

    public static class ScheduleNotFoundException extends RuntimeException {
        public ScheduleNotFoundException() {
            super("일정을 찾을 수 없습니다.");
        }
    }
}
