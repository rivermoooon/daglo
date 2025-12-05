# Rick and Morty Character App

## 주요 기능

- **캐릭터 목록**: Paging3를 활용한 효율적인 캐릭터 목록 표시 및 무한 스크롤
- **캐릭터 검색**: 실시간 검색 기능 (debounce 적용)
- **상세 화면**: 캐릭터 상세 정보 표시
- **Shared Element Transition**: 리스트와 상세 화면 간 부드러운 이미지 전환 애니메이션
- **다크 테마**: 시스템 테마 자동 감지 및 수동 전환 지원
- **에러 처리**: 네트워크 에러를 사용자 친화적인 메시지로 표시
- **Pull-to-Refresh**: 목록 새로고침 기능

## 아키텍처

이 프로젝트는 **Clean Architecture**와 **MVI (Model-View-Intent) 패턴**을 따릅니다.

```
┌─────────────────────────────────────┐
│      Presentation Layer              │
│  (Feature: List, Detail, Search)     │
│  - ViewModel (MVI)                   │
│  - Compose UI                        │
│  - UiModel                           │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│          Domain Layer               │
│  - Repository Interface             │
│  - Domain Models                    │
│  - DataResource                     │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│           Data Layer                │
│  - Repository Implementation        │
│  - Remote DataSource                │
│  - PagingSource                     │
│  - Data Mapper                      │
└─────────────────────────────────────┘
```

### MVI 패턴 구조

- **Intent**: 사용자 액션을 `UiIntent`로 표현 (예: `CharacterListIntent.OnCharacterClick`)
- **State**: 불변 상태 객체 `UiState` (예: `CharacterListUiState`)
- **SideEffect**: 일회성 이벤트 (예: `CharacterDetailEffect.ShowError`)
- **단방향 데이터 흐름**: View → Intent → ViewModel → State → View

```
View → onIntent() → ViewModel.processIntent() → setState() → View (collectAsState)
```

## 📦 프로젝트 구조

```
daglo/
├── app/                    # 애플리케이션 진입점
│   ├── MainActivity        # 메인 액티비티 (테마 관리)
│   ├── MainViewModel       # 테마 상태 관리
│   ├── DagloApp            # 루트 Composable
│   └── AppNavHost          # 네비게이션 호스트
│
├── feature/                # Presentation Layer
│   ├── list/              # 캐릭터 목록 화면
│   │   ├── CharacterListScreen
│   │   ├── CharacterListViewModel
│   │   ├── CharacterListContract
│   │   └── component/
│   │       └── DagloImageCard
│   ├── detail/            # 캐릭터 상세 화면
│   │   ├── CharacterDetailScreen
│   │   ├── CharacterDetailViewModel
│   │   └── CharacterDetailContract
│   ├── search/            # 캐릭터 검색 화면
│   │   ├── CharacterSearchScreen
│   │   ├── CharacterSearchViewModel
│   │   └── CharacterSearchContract
│   ├── model/             # UiModel
│   │   ├── CharacterDetailUiModel
│   │   ├── CharacterListItem
│   │   └── CharacterSearchItem
│   └── CharacterNavigation
│
├── domain/                 # Domain Layer
│   ├── model/             # 도메인 모델
│   │   └── Character
│   ├── repository/        # 리포지토리 인터페이스
│   │   ├── CharacterRepository
│   │   └── ThemeRepository
│   └── DataResource       # 데이터 리소스 상태
│
├── data/                   # Data Layer
│   ├── impl/              # Repository 구현
│   │   ├── CharacterRepositoryImpl
│   │   └── ThemeRepositoryImpl
│   ├── remote/            # 원격 데이터 소스
│   │   └── RemoteCharacterDataSource
│   ├── paging/            # PagingSource
│   │   └── CharacterPagingSource
│   ├── model/             # 데이터 모델
│   │   └── CharacterEntity
│   ├── mapper/            # 데이터 매퍼
│   │   └── CharacterMapper
│   ├── DataErrorMapper    # 에러 매퍼
│   └── FlowDataResource   # Flow 래퍼 유틸리티
│
└── core/                   # 공통 모듈
    ├── common/            # 공통 유틸리티
    │   └── AppError       # 에러 타입 정의
    ├── common-ui/         # 공통 UI 유틸리티
    ├── network/           # 네트워크 설정
    │   ├── service/       # API 서비스
    │   ├── model/         # 네트워크 모델
    │   ├── impl/          # DataSource 구현
    │   └── RemoteMapper   # 네트워크 매퍼
    ├── datastore/         # 로컬 데이터 저장소
    │   ├── LocalThemeDataSource
    │   └── impl/
    ├── designsystem/      # 디자인 시스템
    │   ├── component/     # 재사용 가능한 컴포넌트
    │   │   ├── DagloImage
    │   │   ├── DagloTopBar
    │   │   └── SnackBar
    │   └── foundation/     # 테마, 색상, 타이포그래피
    └── navigation/        # 네비게이션
        └── DagloRoute
```

## 🛠️ 기술 스택

### Core
- **Kotlin**
- **Jetpack Compose**
- **Kotlin Coroutines**
- **Flow**
- **Kotlinx Serialization**

### Architecture
- **Clean Architecture**: 레이어 분리로 테스트 용이성 및 유지보수성 향상
- **MVI Pattern**: Intent 기반 단방향 데이터 흐름으로 상태 관리 일관성 확보
- **Hilt**: 의존성 주입으로 결합도 감소

### Network
- **Retrofit**: REST API 통신
- **OkHttp**: HTTP 클라이언트 및 로깅 인터셉터
- **kotlinx-serialization**: JSON 직렬화/역직렬화

### Data Persistence
- **DataStore**: 테마 설정 등 키-값 데이터 저장

### Paging
- **Paging3**: 대용량 데이터 효율적 처리 및 무한 스크롤

### Image Loading
- **Coil**: 비동기 이미지 로딩 라이브러리

### Navigation
- **Navigation Compose**: 화면 간 네비게이션
- **Hilt Navigation Compose**: 네비게이션과 Hilt 통합

### Animation
- **Shared Element Transition**: 리스트와 상세 화면 간 부드러운 전환 애니메이션

## 📋 주요 모듈 설명

### feature:list
캐릭터 목록 화면
- **CharacterListContract**: UiState, UiIntent, SideEffect 정의
- **CharacterListViewModel**: Intent 기반 상태 관리 (MVI)
- **CharacterListScreen**: Compose UI, Paging3를 활용한 무한 스크롤
- **DagloImageCard**: 캐릭터 카드 컴포넌트 (Shared Element Transition 지원)

### feature:detail
캐릭터 상세 화면
- **CharacterDetailContract**: UiState, UiIntent, SideEffect 정의
- **CharacterDetailViewModel**: Intent 기반 상태 관리 (MVI)
- **CharacterDetailScreen**: 캐릭터 상세 정보 표시
- Shared Element Transition으로 리스트에서 부드럽게 전환

### feature:search
캐릭터 검색 화면
- **CharacterSearchContract**: UiState, UiIntent, SideEffect 정의
- **CharacterSearchViewModel**: Intent 기반 상태 관리, debounce 적용
- **CharacterSearchScreen**: 실시간 검색 결과 표시

### data
데이터 레이어
- **CharacterRepositoryImpl**: 리포지토리 구현
- **CharacterPagingSource**: Paging3 데이터 소스
- **DataErrorMapper**: 네트워크 에러를 `AppError`로 변환
- **FlowDataResource**: `Flow<DataResource<T>>` 래퍼 유틸리티

### domain
도메인 레이어
- **Character**: 도메인 모델
- **CharacterRepository**: 리포지토리 인터페이스
- **ThemeRepository**: 테마 리포지토리 인터페이스
- **DataResource**: 데이터 로딩 상태 (Loading, Success, Error)

### core:network
네트워크 모듈
- **ApiService**: Retrofit API 서비스 인터페이스
- **RemoteCharacterDataSource**: 원격 데이터 소스 인터페이스
- **RemoteCharacterDataSourceImpl**: 원격 데이터 소스 구현
- **CharacterDto**: 네트워크 응답 모델
- **RemoteMapper**: DTO → Entity 변환

### core:datastore
로컬 데이터 저장소 모듈
- **LocalThemeDataSource**: 테마 데이터 소스 인터페이스
- **LocalThemeDataSourceImpl**: DataStore를 사용한 구현

### core:designsystem
디자인 시스템 모듈
- **DagloTheme**: Material 3 기반 테마
- **DagloImage**: Coil 기반 이미지 컴포넌트 (Shared Element Transition 지원)
- **DagloTopBar**: 커스텀 TopAppBar
- **DagloSnackBarHost**: 커스텀 SnackBar (에러 표시용)

### core:common
공통 모듈
- **AppError**: 애플리케이션 레벨 에러 타입 정의
  - `NetworkError`, `ServerError`, `TimeoutError`, `ParseError`, `NotFoundError`, `AuthError`, `UnknownError`
  - `getUserMessage()`: 사용자 친화적 메시지 제공

## 📱 화면 구성

### Character List Screen
- 2열 그리드 레이아웃
- Pull-to-Refresh
- 무한 스크롤 (Paging3)
- 캐릭터 카드 (이미지, 이름, 상태)
- 검색 아이콘 (검색 화면으로 이동)

### Character Detail Screen
- 캐릭터 이미지 (Shared Element Transition)
- 캐릭터 정보 (이름, 상태, 종족, 성별, 출신지, 위치 등)
- 뒤로가기 버튼

### Character Search Screen
- 검색 입력 필드 (debounce 적용)
- 검색 결과 목록
- 검색 결과 없음 상태 표시
- Shared Element Transition 지원

## 🔧 에러 처리

Clean Architecture 원칙에 따라 에러 처리가 분리되어 있습니다:

- **core:common**: `AppError` (에러 모델), `getUserMessage()` (사용자 메시지)
- **data**: `DataErrorMapper` (네트워크 에러 → AppError)
- **feature**: `SideEffect.ShowError` (UI 표시)

### 에러 처리 흐름

```
Throwable → DataErrorMapper.toAppError() → AppError → getUserMessage() → SnackBar
```

### 에러 타입

- **NetworkError**: 네트워크 연결 문제
- **ServerError**: HTTP 4xx, 5xx 에러
- **TimeoutError**: 요청 타임아웃
- **ParseError**: 데이터 파싱 실패
- **NotFoundError**: 리소스를 찾을 수 없음
- **AuthError**: 인증/인가 실패
- **UnknownError**: 알 수 없는 에러

## 🎨 Shared Element Transition

리스트 화면의 캐릭터 이미지를 클릭하면 상세 화면으로 부드럽게 전환됩니다.

### 구현 방식

1. **SharedTransitionLayout**: 루트 레벨에서 Shared Transition Scope 제공
2. **sharedElement modifier**: 이미지에 적용하여 전환 대상 지정
3. **rememberSharedContentState**: 전환 상태 관리
4. **boundsTransform**: 전환 애니메이션 커스터마이징

### 주요 특징

- 리스트와 상세 화면 간 자연스러운 이미지 전환
- 뒤로가기 시 역방향 애니메이션 지원
- 검색 화면에서도 동일한 전환 지원

## 🌙 다크 테마

시스템 테마를 자동으로 감지하고, 사용자가 설정한 테마를 DataStore에 저장합니다.

### 구현 방식

1. **MainActivity**: 시스템 테마 감지 및 저장된 테마 조회
2. **MainViewModel**: `ThemeRepository`를 통해 테마 상태 관찰
3. **ThemeRepository**: DataStore를 사용한 테마 설정 저장/조회
4. **SideEffect**: 테마 변경 시 상태바 아이콘 색상 자동 업데이트

## 📝 주요 설계 결정

1. **Clean Architecture**: 레이어 분리로 테스트 용이성 및 유지보수성 향상
2. **MVI Pattern**: Intent 기반 단방향 데이터 흐름으로 상태 관리 일관성 확보
3. **Paging3**: 대용량 데이터 효율적 처리
4. **UiModel Layer**: 메모리 최적화 및 레이어 분리 강화
5. **Shared Element Transition**: 사용자 경험 향상
6. **에러 처리**: 중앙화된 에러 처리로 일관성 있는 사용자 경험 제공

## 🔄 데이터 흐름

### 캐릭터 목록 로딩

```
CharacterListScreen
  → CharacterListViewModel.getPagedCharacters()
  → CharacterRepository.getPagedCharacters()
  → CharacterPagingSource.load()
  → RemoteCharacterDataSource.getCharacters()
  → ApiService.getCharacters()
  → Flow<PagingData<Character>>
  → LazyPagingItems
  → CharacterListScreen (UI 업데이트)
```

### 캐릭터 검색

```
CharacterSearchScreen
  → CharacterSearchViewModel.searchQueryFlow
  → debounce(300ms)
  → CharacterRepository.searchCharacters()
  → RemoteCharacterDataSource.searchCharacters()
  → ApiService.searchCharacters()
  → Flow<DataResource<List<Character>>>
  → CharacterSearchScreen (UI 업데이트)
```

## 📚 Open API

이 프로젝트는 [Rick and Morty API](https://rickandmortyapi.com/)를 사용합니다.

Rick and Morty API는 Rick and Morty 시리즈의 캐릭터, 위치, 에피소드 정보를 제공하는 RESTful API입니다.

## 🏗️ 모듈화 전략

- **재사용성**: 재사용 가능한 코드를 적절히 모듈화하여 코드 공유 기회 제공
- **병렬 빌드**: 각 모듈을 병렬로 빌드하여 빌드 시간 단축
- **엄격한 가시성 제어**: 모듈이 전용 컴포넌트만 노출하고 다른 레이어에 대한 접근을 제한하여 잘못된 사용 방지
- **분산 집중**: 각 개발 팀이 전용 모듈에 할당되어 자신의 모듈에 집중 가능
