# 학습 관리 시스템(Learning Management System)
## 진행 방법
* 학습 관리 시스템의 수강신청 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 요구사항
### 1단계 - 레거시 코드 리팩터링
- [X] Question을 deleted True로 변경한다.
- [X] Question을 삭제할 시 loginUser가 일치하지 않으면 예외가 발생한다.
- [X] Answer를 deleted True로 변경한다.
- [X] Answer 작성자가 loginUser와 일치하지 않으면 예외가 발생한다.
- [X] DateHistory List를 생성한다.
- [X] Answers 일급컬렉션을 생성한다.

### 2단계 - 수강신청(도메인 모델)
- [X] 과정을 기수 단위로 생성한다.
- [X] 과정에 여러개의 강의를 생성한다.
- [X] 이미지를 생성한다.
- [X] 이미지가 1MB를 초과하면 예외가 발생한다.
- [X] 이미지의 너비가 300px, 높이가 200px 미만이면 예외가 발생한다.
- [X] 이미지의 너비 높이가 3:2 비율이 아니라면 예외가 발생한다.
- [X] gif, jpg, jpeg, png, svg 확장자가 아니면 예외를 발생시킨다.
- [X] 무료 강의를 생성한다.
- [X] 유료 강의를 생성한다.
- [X] 강의를 생성할 시 시작일보다 종료일이 빠르면 예외가 발생한다.
- [X] 강의를 모집중으로 변경한다.
- [X] 강의를 종료한다.
- [X] 무료 강의를 신청할 시 수강인원이 1 증가한다.
- [X] 유료 강의를 신청할 시 수강생이 결제한 금액과 수강료가 일치하지 않으면 예외가 발생한다.
- [X] 강의 신청시 강의가 모집중이 아니라면 예외가 발생한다.
- [X] 유료 강의를 신청할 시 결제내역이 없으면 예외가 발생한다.
- [X] 강의가 이미 만석이면 예외를 발생한다.
- [X] 결재 내역을 생성한다.

### 3단계 - 수강신청(DB 적용)
- [X] 과정을 저장한다.
- [X] 과정을 조회한다.
- [X] 강의를 저장한다.
- [X] 강의를 조회한다.
- [X] 이미지를 저장한다.
- [X] 이미지를 조회한다.
- [X] SessionService에서 강의와 이미지를 함께 저장한다.
- [X] 이미지를 SessionService에서 강의와 이미지를 함께 조회한다.
- [X] 강의의 상태를 변경한다.
- [X] 강의 신청자를 저장한다.

### 4단계 - 수강신청(요구사항 변경)
- [X] 강의는 강의 커버 이미지 정보를 가진다.
- [ ] 강의의 모집 상태를 추가하여, 모집중이 아니면 강의신청이 되지 않는다.
- [X] 승인자들에 한 신청가능한 강의는 승인인원에 포함되어있어야한다.