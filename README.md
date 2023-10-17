# 백엔드 Pro 과정 미니 기술과제

-------------------------------

> 빌드 결과물 링크 - https://github.com/JackCme/zero-base-mini-exam/releases/download/v0.0.1/link-0.0.1-SNAPSHOT.jar

## API 명세서

### 회원 초대 - `POST /invite/member`

> 🚀 
> 회원과 초대 코드를 신규로 생성합니다. 
> - 중복 회원을 방지하기 위해 이메일을 조회 키로 사용합니다.
> - 요청한 이메일로 이미 임시회원이 생성되어 있다면 신규로 생성하지 않고 새로운 초대코드를 발급합니다.
> - 요청한 회원이 이미 활성화 상태일 시 에러코드를 내보냅니다.
> - 전화번호와 이메일의 형식이 올바르지 않으면 에러코드를 내보냅니다.

#### Request
`Content-Type: application/json`

`Request Body Parameters`

  | Name          | Type   | Required | Description |
    |---------------|--------|----------|-----------|
  | `name`        | String | ✅        | 회원 이름 |
  | `email`       | String | ✅        | 회원 이메일(중복 불가) |
  |  `phoneNumber` | String | ✅         | 회원 전화번호 |

#### Response example

```json
{
  "memberId": 1,  // 신규 생성 혹은 해당 이메일로 조회된 회원의 ID
  "inviteCode": "015FTGg", // 초대 코드
  "createdAt": "2023-10-17T22:39:12.075748"
}
```

#### Bad Request example
```json
{
  "status": 400,
  "message": "Email is not valid",
  "errorCode": "EMAIL_NOT_VALID"
}
```

### 초대 수락 - `POST /invite/accept`

> 🎉
> 요청한 회원의 초대 코드를 초대수락 처리 합니다.
> - 해당 회원의 초대코드를 만료 시키고 회원을 활성화 시킵니다.
> - 이미 만료된 초대코드를 요청 시 오류코드를 내보냅니다.
> - 요청한 초대코드의 소유자가 해당 회원이 아닐 시 오류코드를 내보냅니다.


`Content-Type: application/json`

`Request Body Parameters`

| Name          | Type    | Required | Description     |
    |---------------|---------|----------|-----------------|
| `inviteCode`        | String  | ✅        | 회원의 초대 코드       |
| `memberId`       | Integer | ✅        | 초대코드를 받은 회원의 ID |

#### Response example

```json
{
  "memberId": 1,
  "memberStatus": "ACTIVE" // 회원의 활성 상태
}
```

#### Bad Request example
```json
{
  "status": 404,
  "message": "Member is not found",
  "errorCode": "MEMBER_NOT_FOUND"
}
```