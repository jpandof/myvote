package com.pando.myvote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyVoteService {
    public static final String INDEX = "index";
    public static final String TIE = "tie";
    public static final String ADMIN = "admin";
    Map<String, Integer> mapVotes = new HashMap<>();
    Map<String, Boolean> mapSession = new HashMap<>();
    Integer totalParticipantes = 2;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request){
        int totalVotes = mapVotes.values().stream().mapToInt(Integer::intValue).sum();
        model.addAttribute("totalVotes", totalVotes);
        model.addAttribute("totalParticipantes", totalParticipantes);
        updateResults(model);
        String sessionValue = getSessionValue(request);
        Boolean hasVoted = mapSession.get(sessionValue);

        // check if user already has voted
        if (hasVoted != null && hasVoted) {
            if (totalVotes == totalParticipantes) {
                updateModel(model, "Todos han votado");
            } else {
                updateModel(model, "Esperando al resto de participantes");
            }
        }
        return "index";
    }
    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public String admin(ModelMap model){
        model.addAttribute("totalParticipantes", totalParticipantes);
        return ADMIN;
    }

    @RequestMapping(value="/admin/change", method = RequestMethod.GET)
    public String change(ModelMap model, @RequestParam Integer totalParticipantes){
        this.totalParticipantes = totalParticipantes;
        model.addAttribute("totalParticipantes", totalParticipantes);
        return ADMIN;
    }

    @GetMapping("/stats/")
    public ResponseEntity<Integer> vote(Model model, HttpServletRequest request, HttpServletResponse response) {
        int votosHechos = mapVotes.size();
        int votosPendientes = totalParticipantes - votosHechos;
        return ResponseEntity.ok(votosPendientes);
    }


    @GetMapping("/vote/{value}")
    public String vote(@PathVariable String value, Model model, HttpServletRequest request, HttpServletResponse response) {

        String sessionValue = getSessionValue(request);
        Boolean hasVoted = mapSession.get(sessionValue);
        model.addAttribute("totalParticipantes", totalParticipantes);

        // check if user already has voted
        if (hasVoted != null && hasVoted) {
            updateModel(model, "Ya votaste");
            updateResults(model);
            return INDEX;
        } else {
            mapSession.put(sessionValue, true);
        }

        updateVotes(value);
        updateModel(model, "Voto realizado");
        updateResults(model);
        return INDEX;
    }


    @GetMapping("/admin/vote/{value}")
    public String voteAdmin(@PathVariable String value, Model model, HttpServletRequest request, HttpServletResponse response) {

        String sessionValue = getSessionValue(request);
        Boolean hasVoted = mapSession.get(sessionValue);
        model.addAttribute("totalParticipantes", totalParticipantes);

        // check if user already has voted
        if (hasVoted != null && hasVoted) {
            updateModel(model, "Ya votaste");
            updateResults(model);
            return ADMIN;
        } else {
            mapSession.put(sessionValue, true);
        }

        updateVotes(value);
        updateModel(model, "Voto realizado");
        updateResults(model);
        return ADMIN;
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
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    @GetMapping("/result")
    public String result(Model model) {

        int votosActuales = mapVotes.values().stream().mapToInt(Integer::intValue).sum();

        if (mapVotes.isEmpty() || votosActuales < totalParticipantes) {
            model.addAttribute("message", "Faltan por votar");
            return INDEX;
        }

        updateResults(model);


        model.addAttribute("totalVotes", votosActuales);
        model.addAttribute("totalParticipantes", totalParticipantes);

        return INDEX;

    }

    private void updateResults(Model model) {

        int votosActuales = mapVotes.values().stream().mapToInt(Integer::intValue).sum();
        if (votosActuales < totalParticipantes) {
            return ;
        }


        String winner = null;
        int totalVote = 0;
        for (Map.Entry<String, Integer> entry : mapVotes.entrySet()) {
            Integer vote = entry.getValue();
            if (winner == null) {
                winner = entry.getKey();
                totalVote = vote;
            } else {
                if (totalVote == vote) {
                    winner = TIE;
                } else if (totalVote < vote) {
                    winner = entry.getKey();
                    totalVote = vote;
                }
            }
        }
        if (TIE.equals(winner)) {
            model.addAttribute("winner", "empate");
        } else {
            model.addAttribute("winner", winner);
        }

        StringBuilder results = new StringBuilder();

        mapVotes.entrySet().forEach(entry -> {
            if (!results.isEmpty()) {
                results.append("<br>");
            }
            results.append(entry.getKey() + " : " + entry.getValue());
        });
        model.addAttribute("results", results);
    }

    @GetMapping("/admin/clear")
    public String clear(Model model) {
        mapSession.clear();
        mapVotes.clear();
        model.addAttribute("message", "Reset OK");
        return ADMIN;
    }


}
