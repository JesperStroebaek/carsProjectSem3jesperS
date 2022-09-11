package carsProjectSem3.security.for_security_tests;


import carsProjectSem3.security.dto.UserWithRolesRequest;
import carsProjectSem3.security.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import carsProjectSem3.security.entity.UserWithRoles;
import carsProjectSem3.security.repository.UserWithRolesRepository;


@Service
public class UserWithRolesService {

  private  final UserWithRolesRepository userWithRolesRepository;


  public UserWithRolesService(UserWithRolesRepository userWithRolesRepository) {
    this.userWithRolesRepository = userWithRolesRepository;
  }

  public UserWithRolesResponse addDemoUser(UserWithRolesRequest body){
    if(userWithRolesRepository.existsById(body.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This user name is taken");
    }
    if(userWithRolesRepository.existsByEmail(body.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This email is used by another user");
    }
    UserWithRoles userWithRoles = new UserWithRoles(body);
    userWithRoles.addRole(Role.USER);
    return new UserWithRolesResponse(userWithRolesRepository.save(userWithRoles));
  }

  public UserWithRolesResponse getDemoUser(String id){
    UserWithRoles user = userWithRolesRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    return new UserWithRolesResponse(user);
  }

}
