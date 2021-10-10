package rs.atekom.infosystem.server.bezbednost.jwt;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.server.bezbednost.FKontaktDetailService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private FKontaktDetailService fKontaktDetailService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@RequestMapping(value = "/prijava", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception{
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = fKontaktDetailService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
		}
	
	private void authenticate(String username, String password) throws Exception{
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(username);
		Matcher n = p.matcher(password);
		boolean b = m.find();
		boolean c = n.find();
		if(!b && !c && username.length() < 15 && password.length() < 12) {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				} catch (DisabledException e) {
					System.out.println("Korisnik onemogućen " + username + " lozinka " + password);
					throw new Exception("USER_DISABLED", e);
					} catch (BadCredentialsException e) {
						System.out.println("Neispravan unos za pristup " + username + " lozinka " + password);
						throw new Exception("INVALID_CREDENTIALS", null);
						}
			}else {
				System.out.println("Nedozvoljen unos " + username + " lozinka " + password);
				throw new Exception("INVALID_CREDENTIALS", null);
				}
		}
	
	}
