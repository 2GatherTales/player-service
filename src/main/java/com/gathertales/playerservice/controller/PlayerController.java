package com.gathertales.playerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gathertales.playerservice.model.customprincipal.CustomPrincipal;
import com.gathertales.playerservice.model.player.Player;
import com.gathertales.playerservice.service.GenericService;
import com.gathertales.playerservice.util.Token;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/player")
public class PlayerController {
    @Autowired
    private GenericService<Player> playerRepository;

    @Autowired
    private RestTemplate restTemplate;

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    @RequestMapping(value = "health",
            method = RequestMethod.GET,
            produces =  {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<String> health() {
        try {
            UUID uuid = UUID.randomUUID();
            String response = "OK   " + now();
            return new ResponseEntity<String>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/attack/{id}")
    @PreAuthorize("hasAnyAuthority('role_admin', 'role_user')")
    public ResponseEntity<Player> attack(@PathVariable("id") Long id) {
        try {
            CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            if(principal.getId().equals(String.valueOf(id)) ||  (principal.getUsername().equals("admin"))) {

                restTemplate = new RestTemplate();
                // Check if weapon service is healthy /health
                restTemplate.getForObject("http://localhost:9014/api/weapon/health", String.class );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Token token = Token.INSTANCE.getInstance();

                String tokenvalue = token.getValue();
                headers.set("Authorization", "Bearer "+tokenvalue);
                HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
                ResponseEntity<String> response = restTemplate.exchange("http://localhost:9014/api/weapon/find/"+id,
                        HttpMethod.GET, entity, String.class);
                String weaponString = response.getBody().toString();
                Gson gson = new Gson();
                Map map = gson.fromJson(weaponString, Map.class);

                Player player = playerRepository.find(id);
                player.setWeapon(map);

                return new ResponseEntity<Player>(player, HttpStatus.OK);
            }
            return new ResponseEntity<Player>(
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<Player>(
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "counter/{id}")
    public ResponseEntity<Player> attack(@RequestBody String body, @PathVariable("id") Long id) {
        try {
            System.out.println(body);
            Integer dmg =  ((Map<String, Integer>) new ObjectMapper().readValue(body, Object.class)).get("dmg");
            Player player = playerRepository.find(id);
            player.calcGetAttacked(dmg);
            playerRepository.update(player);
            return new ResponseEntity<Player>(player, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Player>(
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('role_admin', 'role_user')")
    public ResponseEntity<Player> find(@PathVariable("id") Long id) {
        try {
            CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            if(principal.getId().equals(String.valueOf(id)) ||  (principal.getUsername().equals("admin")))
                return new ResponseEntity<Player>(
                        playerRepository.find(id), HttpStatus.OK);
            return new ResponseEntity<Player>(
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<Player>(
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<Iterable<Player>> findAll() {

        try {
            CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            if(!principal.getUsername().equals("admin"))
                return new ResponseEntity<Iterable<Player>>(
                        HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<Iterable<Player>>(
                    playerRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Iterable<Player>>(
                    HttpStatus.BAD_REQUEST);
        }
    }
}
