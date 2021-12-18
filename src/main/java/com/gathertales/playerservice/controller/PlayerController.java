package com.gathertales.playerservice.controller;

import com.gathertales.playerservice.model.customprincipal.CustomPrincipal;
import com.gathertales.playerservice.model.player.Player;
import com.gathertales.playerservice.service.implementation.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping("api/player")
public class PlayerController {
    @Autowired
    private PlayerServiceImpl playerRepository;

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
