# Java_board_Spring_
간단한 게시판 프로젝트를 Spring+Vue 방식으로 구현한 프로젝트 입니다.        
기존 Java_board_Spring에서 받은 피드백을 반영했고, 새롭게 피드백을 받아 지속적으로 개선 예정입니다.

# 피드백 목차
* [이전 Java_board_Spring 피드백](#이전-Java_board_Spring-피드백)
    + [추가 리팩토링](#추가-리팩토링)
    + [Service return 유효성](#service-return-유효성)
    + [Commans Naming](#commans-naming)
    + [Command 패턴의 필요성](#Command-패턴의-필요성)
    + [프로퍼티 네임은 전부 소문자](#프로퍼티-네임은-전부-소문자)
    + [웹 리소스 하위에 요소에는 언더바(_)가 아닌 그냥 대시(-)를 쓸 것](#웹-리소스-하위에-요소에는-언더바(_)가-아닌-그냥-대시(-)를-쓸-것)
* [2023-03-04 신규 피드백](#2023-03-04-신규-피드백)
    + [Mapper 주석 및 컨벤션](#Mapper-주석-및-컨벤션)
    + [RequestMapping 분리하기](#RequestMapping-분리하기)
    + [RequestPart나 Json 없이 바로 DTO로 받기](#RequestPart나-Json-없이-바로-DTO로-받기)
    + [DTO에 Validation 제거하기](#DTO에-Validation-제거하기)
    + [유틸 메소드에 DTO 전달하지 말기](#유틸-메소드에-DTO-전달하지-말기)
    + [일반적인 예외 상황 넘어가기 또는 전역처리](#일반적인-예외-상황-넘어가기-또는-전역처리)
    + [Validator 따로 만들기](#Validator-따로-만들기)
    + [@Value에 대한 Property 분리의 필요성](#@Value에-대한-Property-분리의-필요성)
    + [Vue Proxy 설정](#Vue-Proxy-설정)
    + [Vue에 라이프 사이클 이해하기](#Vue에-라이프-사이클-이해하기)
    + [라이브러리는 그냥 라이브러리다.](#라이브러리는-그냥-라이브러리다.)
    + [Vue에 중복 처리를 공통 모듈로 만들기](#Vue에-중복-처리를-공통-모듈로-만들기)

## 이전 Java_board_Spring 피드백

### 추가 리팩토링
리팩토링은 구조나 설계를 바꾸는 것이 아닌 코드 구성이나 기능별로 나누는 것도 포함입니다.  
코드를 기능별로 나눠서 깔끔하게 분리시키는 것이 좋습니다.  
코드 중간중간 Validation 체크보단 Validation Manager가 따로 존재해 Validation을 관리하는 것도 좋습니다.

피드백 반영: 2023-03-04 피드백 당시에는 일정 분제로 리팩토링을 진행하지 못했습니다. 

### Service return 유효성
Service에서 return을 할 때 성공 여부에 따라 true, false를 반환하는 것은 안좋습니다.  
성공 여부보다는 성공 시 반환되는 값이 더 중요합니다.  
Service 로직이 확장되거나 변화할때, true가 아닌 1, 2, 3 등의 값을 반환하게 될 경우 구조를 바꿔야 하기에 확장성이 떨어집니다.  
실패 여부는 차라리 Exception을 발생시키는 것이 좋습니다.

또한 패스워드 체크 등의 별도 검증은 내부적으로 하거나, 컨트롤러에서 미리 잡아주는 방식도 좋습니다.

피드백 반영: Server 리턴은 int 형식으로 진행되며 Service에서는 처리 관련 Exception을 발생시키고, Controller에서는 유효성에 대한 Exception을 잡아서 처리하도록 변경했습니다.  

### Commans Naming
```java

  private final Map<String, ActionCommand> commands = new HashMap<>();
  public ActionCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("write", new ArticleWriteActionCommand(this.dependencyCommand));
    this.commands.put("comment_write", new CommentWriteActionCommand(this.dependencyCommand));
    this.commands.put("modify", new ArticleModifyActionCommand(this.dependencyCommand));
    this.commands.put("delete", new ArticleDeleteActionCommand(this.dependencyCommand));
  }
```
위와 같이 commands는 List 느낌이 들지만, Map으로 되어있습니다.  
차라리 이름에 Map을 붙이는 것을 추천드립니다.

피드백 반영: 문제가 되던 Command 패턴 네이밍은 제거되면서 해결됬지만 가급적이면 형식을 유추할 수 있는 네이밍을 사용하도록 작업중입니다.  

### Command 패턴의 필요성
Command 패턴을 사용하는 이유는 Command를 통해 Controller와 Service를 분리하기 위함입니다.  
Servlet 방식에서는 직접 구현이 필요하겠지만 Spring 방식에서는 Mapping 기능이 존재하기에 Command 패턴을 사용할 필요가 없습니다.  
Command 패턴을 Spring에서 사용하면 Spring에 라이프 사이클을 벗어나 의존성 주입 등의 혜택을 받을 수 없게 됩니다.

Domain(Comment, Article, File)을 기준으로 File을 분리하고, 각 RequestMapping은 하나의 Command 기능을 맡도록 구성하는걸 추천드립니다.

피드백 반영: Command 패턴을 제거하고 Controller를 도메인 별로 분리해, 각 요청에 대한 액션을 RequestMapping을 활용하는 방식으로 변경했습니다.  
### 프로퍼티 네임은 전부 소문자
프로퍼티 네임은 전부 소문자로 작성하는 것이 좋습니다.
```xml
thymeleaf.list.table.createdDate=등록 일시
```
프로퍼티 이름을 사용 영역별로 나눈다고 해서 대문자를 사용하는 것은 좋지 않습니다.  
기존 이름을 차라리 단축시키거나 분리하는 것을 추천드립니다.
```xml
thymeleaf.list.table.created=등록 일시
```

피드백 반영: 문제가 되던 프로퍼티는 사용하지 않음으로 제거했지만, 가급적이면 프로퍼티 네이밍을 소문자 방식으로 작업중입니다.  

### 웹 리소스 하위에 요소에는 언더바(_)가 아닌 그냥 대시(-)를 쓸 것
리소스 아래에서 쓰이는 모든 요소들에는 언더바 대신 그냥 대시(-)를 쓸 것을 추천드립니다.  
물론 QueryString에서는 카멜케이스를 사용하는 것이 좋습니다.  

피드백 반영: Vue를 도입하며 기존 웹 리소스는 폐기했지만, 가급적이면 웹 리소스 네이밍을 대시 방식으로 작업중입니다.  

## 2023-03-04 신규 피드백

### Mapper 주석 및 컨벤션
Mapper.xml을 작성시 주석을 작성하는 건 좋은 습관입니다.  
다만 이미 Service를 비롯한 모든 요소에 주석이 성실히 적혀있다면 Mapper에서의 주석은 간단한 소개로도 충분합니다.  
또한 Mapper에서는 컬럼명, SELECT 등의 요소들에 대해 컨벤션을 지켜주는 것이 좋습니다.  
```xml
<!--
  article Count 검색을 위한 select query
  검색 조건에 따라 where절에 조건을 추가
  검색 조건은 searchWord(title, writer, content), categoryId, startDate, endDate
 // 위 주석은 상세하지만 가독성을 망칩니다. 간단한 소개정도로 충분합니다.
 // 아래 구문을 보면 like에 대해서 소문자로 적혀있는데 가급적 통일된 네이밍을 추천드립니다.  

    -->
  <select id="selectArticleCount" resultType="int"
    parameterType="com.board.spring.rest.yoony.article.search.SearchDTO">
    SELECT COUNT(article_id) FROM article
    <where>
      <if
        test="!@com.board.spring.rest.yoony.util.ValidationChecker@isEmpty(searchWord)">
        (
        title like CONCAT('%',#{searchWord},'%')
        OR content like CONCAT('%',#{searchWord},'%')
        OR writer like CONCAT('%',#{searchWord},'%')
        )
      </if>
    </where>
    ORDER BY article_id DESC
  </select>
```

### RequestMapping 분리하기
현재 프로젝트는 도메인을 기준으로 Controller가 구분되어 있고 각 Controller에 들어오는 Action에 따라서 RequestMapping이 분리되어 있습니다.  
지금 같은 소규모에 분리가 명확한 경우에는 괜찮은 방식이지만, 프로젝트가 커지면 분리가 어려워집니다.  
예시:  
```java

// Controller는 특정 게시글에 Comments로 들어오는 모든 요청을 매핑합니다.
@RestController
@RequestMapping("/v1/articles/{articleId}/comments")
public class CommentController {
  
  // 아래의 두 요청은 Post, Get 단위로 분리되어 있습니다.
  // 규모가 커진다면 Post, Get 요청이 다른 처리로 중복될 수 있습니다.   
  @PostMapping
  public ResponseEntity createComment(@PathVariable long articleId, @RequestBody CommentDTO commentDTO)
      throws CustomException, Exception {
    // 처리 코드
  }
  @GetMapping
  public ResponseEntity getCommentList(@PathVariable long articleId)
      throws CustomException, Exception {
    // 처리 코드
  }
}
```

### RequestPart나 Json 없이 바로 DTO로 받기
파일과 DTO 정보를 보내는 경우 DTO를 JSON으로 변환시켜 보내는 경우가 있습니다.
예제:   
```java
  @PostMapping
  public ResponseEntity createArticle(@RequestPart("articleDTO") String articleDTOJson,
      @RequestPart(value = "files", required = false) MultipartFile[] files) {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleDTO articleDTO = objectMapper.readValue(articleDTOJson, ArticleDTO.class);
  }
```
이 경우 JSON을 다시 DTO로 파싱하는 과정이 별도로 필요하게 됩니다.  
가급적 DTO를 그대로 받아서 사용하는 것을 추천드립니다.  

### DTO에 Validation 제거하기
DTO를 데이터 전달 객체로 사용하는 경우 Validation을 제거하는 것을 추천드립니다.  
DTO가 통신을 통해 Front로 전달되는 경우 안에 존재하는 Validation Method는 모두 내부 로직을 연산하고 처리해 그 결과값을 저장해 반환됩니다.  
사용하지도 않을 Method를 연산하고 반환하는 것은 데이터 낭비가 됩니다.  
```java
  private String writer;

  // 아래에 Validation 메소드는 연산이 되고 결과값이 반환됩니다.
  public boolean isWriterValid() {
    return writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$");
  }

```
### 유틸 메소드에 DTO 전달하지 말기
util 메소드에 DTO를 전달하는 것은 좋지 않습니다.  
util은 어디서든 호출해 사용 할 수 있도록 특정 클래스에 의존성을 가지도록 하는 것은 좋은 방법이 아닙니다.  

```java
// 아래 메소드는 처리를 위해 FileDTO를 전달받습니다.
// 다만 정말로 필요한 것은 FileName과 FileUploadPath입니다. 
// 전달되는 매개변수가 늘어나도 DTO와 분리시키는 것이 좋습니다.  
public static ResponseEntity downloadFile(FileDTO fileDTO, List<String> rangeHeader) throws Exception{

}
```

### 일반적인 예외 상황 넘어가기 또는 전역처리
모든 예외를 상세하게 하나씩 다 잡을수는 없습니다.  
로직상으로 큰 문제를 일으키지 않는 예외는 넘어가도록 처리하는 것이 좋습니다.
만약 간단하지만 반복되어야할 에러라면 전역적으로 처리하는 방법을 고려하는 것도 좋습니다.  
```java
  // 특정 게시글을 조회하기 위한 Id 값이 0 인경우 예외를 발생합니다.
  // 하지만 처리하지 않아도 Service에서 예외가 발생하거나 결과값을 반환하지 않기 때문에 필수적인 예외처리는 아닙니다. 
   if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
```

### Validator 따로 만들기
이전 예외의 전역처리에 대한 피드백과 어느정도 일치하는 내용입니다.  
반복되거나 전역적으로 처리해야하는 예외들에 대한 Validator를 따로 만들어서 처리하는 것을 추천드립니다.  
어노테이션을 이용해 처리하는 방법도 있습니다.

### @Value에 대한 Property 분리의 필요성
@Value 어노테이션은 원할때 프로퍼티를 가져다 사용하기 위한 의도입니다. 
굳이 프로퍼티를 분리하지 않아도 되는 경우에는 @Value를 사용하는 것이 좋습니다.  
만약 분리를 원한다면 static final을 통해서 즉각적으로 가져올 수 있도록 처리하는게 좋습니다.  
```java
// 프로퍼티로 분리되었지만, 필요성에 대해서는 고민이 필요합니다.  
@Component
@Data
public class FileProperty {
  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;
}
```

### Vue Proxy 설정
Vue와 Spring을 연동하기 위해 proxy를 쓰는 경우가 있습니다.  
proxy는 내부적인 요청을 다른 서버로 요청을 돌리기 위한 설정이기 때문에 내부 연동을 위해서는 추천드리지 않습니다.  
내부 연동을 위해서라면 차라리 url을 환경변수로 잡고 Axios에서 환경변수를 사용하는 것이 좋습니다.  

vue.config.js 예제: 
```js
  // vue는 8080 포트를 사용하고 proxy로 8081로 요청을 돌립니다.
  devServer: {
    proxy: "http://localhost:8081",
  },
```

### Vue에 라이프 사이클 이해하기
Vue에는 자원과 렌더링을 관리하기위한 라이프 사이클이 존재합니다.  
Vue를 사용한다면 라이프 사이클을 사용해 효율적인 프론트 제공을 해야합니다.  
Composition api를 사용하며, setup() 메소드에 모든 처리와 관리를 하는 경우가 많습니다.  
내부 로직에서 각 라이프 사이클에 분리가 가능한 부분이나 어느 시점에 구성되고 쓰여야 하는지를 고민하고 개발하는 과정이 필요합니다.  
또한 이벤트 버스에 대한 고민을 해보시는 것을 추천합니다.  
프론트 개발은 어떤 액션을 만들고, 어떻게 라이프 흐름을 따라가고 이해하고 적용시키는지 위주의 훈련을 해야합니다.  

### 라이브러리는 그냥 라이브러리다.
Vue를 사용하기 위해 Vuetify와 같은 디자인 라이브러리를 공부하고 사용하는 경우가 많습니다.  
라이브러리는 결국 변화할 수 있고 사라질 수 도 있습니다.  
라이브러리보다는 원론적인 Vue에 대한 공부와 투자를 좀더 하는게 좋은 방향성입니다.  
예를 들어 Vuetify에 v-data-table을 사용하면 데이터에 대한 테이블 디자인과 페이지네이션또한 제공해줍니다.  
이러한 라이브러리 기능을 활용하기 보단 직접 Table과 Pagination을 구현하고 Vue 라이프 사이클에서 어떻게 관리할지와 같은 기본 기능에 집중하는 것이 좋습니다.  

### Vue에 중복 처리를 공통 모듈로 만들기
Vue또한 Spring과 같이 중복 처리를 공통 모듈로 만들어 사용하는 것이 좋습니다.  
예를 들어 Axios를 사용해 API를 호출하는 경우가 많습니다.  
Axios 요청에 대한 공통 처리를 위해 인터럽트를 사용하는데, 
이러한 Axios를 공통 모듈로 만들어 사용하는 것이 좋습니다.  
유효성 검증과 같은 경우도 공통 모듈로 만들어 사용하는 것이 좋습니다.  


