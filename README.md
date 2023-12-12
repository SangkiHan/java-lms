# 학습 관리 시스템(Learning Management System)
## 진행 방법
* 학습 관리 시스템의 수강신청 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## 기능 목록
## LMS
* `Question` : 질문
    * [x] : 질문 작성자와 사용자가 같은지 확인한다.
    * [x] : 질문의 상태를 삭제 상태로 바꾼다.
    * [x] : 답변 삭제 이력을 생성한다.

* `Answer` : 답변
    * [x] : 답변 작성자와 사용자가 같은지 확인한다.
    * [x] : 답변 작성자와 질문 작성자가 같은지 확인한다.
    * [x] : 답변의 상태를 삭제 상태로 바꾼다.
    * [x] : 답변 삭제 이력을 생성한다.

* `DeleteHistory` : 삭제 이력

---
* `SessionUser` : 신청 사람(강사, 학생)
  * [x] : 동일한 학생이 동일 세션에 2번 이상 신청할 수 없습니다.
  * [x] : 전체 수강 신청 인원 중에서 승인된 사람들의 명수를 확인할 수 있습니다.
  * [x] : 강사는 신청한 사람들을 취소할 수 있다.

* `Session` : 강의
  * [x] : 강사는 자유롭게 등록 가능합니다.
  * [x] : 동일한 학생이 동일 세션에 2번 이상 신청할 수 없습니다.
  * [x] : 유료 강의라면 최대 수강 인원까지만 수강 신청 가능합니다.
  * [x] : 모집중인 강의만 신청 가능합니다.
  * [x] : 종료날짜가 시작날짜보다 이후여야 합니다.
  * [x] : 강의는 하나 이상의 커버 이미지를 가질 수 있습니다.

* `Course` : 코스
  * [x] : 새로운 코스를 등록합니다.
  * [x] : 새로운 세션을 추가합니다.
  * [x] : 이미지 크기는 1MB 이하입니다.
  * [x] : 이미지 타입은 gif, jpg, jpeg, png, svg만 허용됩니다.
  * [x] : width : 300px, height : 200px 이상
  * [x] : width : height = 3: 2 입니다.

