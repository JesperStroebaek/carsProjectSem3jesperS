package carsProjectSem3.cars.api;

import carsProjectSem3.cars.dto.MemberRequest;
import carsProjectSem3.cars.dto.MemberResponse;
import carsProjectSem3.cars.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
public class MemberController {
  MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  //Security --> Non Authenticated
  //@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  @PostMapping // same as above when you are using @RestController
  public MemberResponse addMember(@RequestBody MemberRequest body) {
    return memberService.addMember(body);
  }


  //Security Admin, but when using security whe can get the username for the "logged in" user and let him edit him self
  @PutMapping("/{username}")
  public ResponseEntity<Boolean> editMember(@RequestBody MemberRequest body, @PathVariable String username){
    memberService.editMember(body,username);
    return new ResponseEntity<>(true,HttpStatus.OK);
  }

  //Security ADMIN
  @PatchMapping("/ranking/{username}/{value}")
  public void setRankingForUser(@PathVariable String username, @PathVariable int value){
    memberService.setRankingForUser(username,value);
  }

  //Security ADMIN
  @GetMapping
  public List<MemberResponse> getMembers(){

    return memberService.getMembers();

  }


  /* JUST TO SHOW HOW NOT TO DO IT
  @Autowired
  MemberRepository memberRepository;
  @GetMapping("/bad")
  public List<Member> getMembersBad(){
    return memberRepository.findAll();
  }
  ***********/

  //../api/members/{username}
  //Security Admin, but when using security whe can get the username for the "logged in" user and let him edit himself
  @GetMapping(path = "/{username}")
  public MemberResponse getMemberById(@PathVariable String username) throws Exception { //Obviously we need to be able to limit this in a system with thousands of members
     MemberResponse response = memberService.findMemberByUsername(username);
     return response;
  }

  //Security ADMIN
  @DeleteMapping("/{username}")
  public void deleteMemberByUsername(@PathVariable String username){
    memberService.deleteByUsername(username);
  }

}
