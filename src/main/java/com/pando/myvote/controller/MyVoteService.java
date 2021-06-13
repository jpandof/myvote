package com.pando.myvote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class MyVoteService {
    public static final String SESSION = "session";
    public static final String INDEX = "index";
    public static final String TIE = "tie";
    Map<String, Integer> mapVotes = new HashMap<>();
    Map<String, Boolean> mapSession = new HashMap<>();

    @GetMapping("/vote/{value}")
    public String vote(@PathVariable String value, Model model, HttpServletRequest request, HttpServletResponse response) {

        String sessionValue = getSessionValue(request);
        addCookie(response, sessionValue);
        Boolean hasVoted = mapSession.get(sessionValue);

        // check if user already has voted
        if (false && hasVoted != null && hasVoted) {
            updateModel(model, "Ya votaste");
            return INDEX;
        } else {
            mapSession.put(sessionValue, true);
        }

        updateVotes(value);
        updateModel(model, "Voto realizado");
        return INDEX;
    }

    private void updateModel(Model model, String message) {
        model.addAttribute("totalVotes", mapVotes.values().stream().mapToInt(Integer::intValue).sum());
        model.addAttribute("message", message);
    }

    private void updateVotes(String value) {
        Integer totalVotesInValue = mapVotes.putIfAbsent(value, 1);
        if (totalVotesInValue != null) {
            totalVotesInValue++;
            mapVotes.put(value, totalVotesInValue);
        }
    }

    private String getSessionValue(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (SESSION.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return UUID.randomUUID().toString();
    }

    private void addCookie(HttpServletResponse response, String sessionValue) {
        // create a cookie

        Cookie cookie = new Cookie(SESSION, sessionValue);

        // expires in 1 day
        cookie.setMaxAge(24 * 60 * 60);

        // optional properties
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);
    }

    @GetMapping("/result")
    public String result(Model model) {

        if (mapVotes.isEmpty()) {
            model.addAttribute("message", "Sin resultados");
        }

        String winner = null;
        int totalVote = 0;
        for (Map.Entry<String, Integer> entry : mapVotes.entrySet()) {
            if (winner == null) {
                Integer vote = entry.getValue();
                if (totalVote == vote) {
                    winner = TIE;
                } else if (totalVote < vote) {
                    winner = entry.getKey();
                    totalVote = vote;
                }
            }
        }
        if (TIE.equals(winner)){
            model.addAttribute("winner", "empate");
        } else  {
            model.addAttribute("winner", winner);
        }
        model.addAttribute("totalVotes", mapVotes.values().stream().mapToInt(Integer::intValue).sum());
        StringBuilder results = new StringBuilder();

        mapVotes.entrySet().forEach(entry -> {
            if (!results.isEmpty()){
                results.append("<br>");
            }
            results.append(entry.getKey() + " : " + entry.getValue());
        });
        model.addAttribute("results", results);
        return INDEX;

    }

    @GetMapping("/clear")
    public String clear() {
        mapSession.clear();
        mapVotes.clear();
        return INDEX;
    }


}
