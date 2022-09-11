package carsProjectSem3.cars.service;


import carsProjectSem3.cars.dto.MemberRequest;
import carsProjectSem3.cars.dto.MemberResponse;
import carsProjectSem3.cars.entity.Member;
import carsProjectSem3.cars.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MemberService {
  private MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public MemberResponse addMember(MemberRequest memberRequest){
    //Later you should add error checks --> Missing arguments, email taken etc.

      if(memberRepository.existsById(memberRequest.getUsername())){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID already exist");
      }
      if(memberRepository.existsByEmail(memberRequest.getEmail())){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this Email already exist");
      }

      Member newMember = MemberRequest.getMemberEntity(memberRequest);
      newMember = memberRepository.save(newMember);

      return new MemberResponse(newMember, false);
  }

 public void editMember(MemberRequest body, String username){
    Member member = memberRepository.findById(username).orElseThrow(
            ()->  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this username dont exist"));

    if(!body.getUsername().equals(username)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot change username");
    }
    member.setPassword(body.getPassword());
    member.setEmail(body.getEmail());
    member.setFirstName(body.getFirstName());
    member.setLastName(body.getLastName());
    member.setStreet(body.getStreet());
    member.setCity(body.getCity());
    member.setZip(body.getZip());
    memberRepository.save(member);
  }

  public List<MemberResponse> getMembers() {
    List<Member> members = memberRepository.findAll();

    List<MemberResponse> response = members.stream().map(member -> new MemberResponse(member,false)).collect(Collectors.toList());

    return response;
  }

  public MemberResponse findMemberByUsername(@PathVariable String username) throws Exception {
    Member found = memberRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    return new MemberResponse(found,false);
  }

  public void setRankingForUser(String username, int value) {
    Member member = memberRepository.findById(username).orElseThrow(()->  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this username already exist"));
    member.setRanking(value);
    memberRepository.save(member);
  }

  public void deleteByUsername(String username) {
    memberRepository.deleteById(username);
  }
}
