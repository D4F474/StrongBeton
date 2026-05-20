package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.ClanDTO;
import com.strongBeton.strongBeton.DTO.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.DTO.ClanMemberDTO;
import com.strongBeton.strongBeton.DTO.ExerciseDTO;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.ClanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/clans")
public class ClanRestController {

    private final ClanService clanService;

    @Autowired
    public ClanRestController(ClanService clanService) {
        this.clanService = clanService;
    }

    @PostMapping
    public ResponseEntity<ClanDTO> createClan(@RequestBody ClanDTO clanDTO, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clanService.createClan(clanDTO, user));
    }

    @GetMapping("/{clanId}")
    public ResponseEntity<ClanDTO> getClanById(@PathVariable int clanId) {
        return ResponseEntity.ok(clanService.getClanById(clanId));
    }

    @PutMapping("/{clanId}")
    public ResponseEntity<ClanDTO> updateClan(@PathVariable int clanId,
                                              @RequestParam UUID userId,
                                              @RequestBody ClanDTO clanDTO) {
        return ResponseEntity.ok(clanService.updateClan(clanId, userId, clanDTO));
    }

    @DeleteMapping("/{clanId}")
    public ResponseEntity<Void> deleteClan(@PathVariable int clanId,
                                           @RequestParam UUID userId) {
        clanService.deleteClan(clanId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClanDTO>> searchClans(@RequestParam String name) {
        return ResponseEntity.ok(clanService.searchClans(name));
    }

    @GetMapping("/top")
    public ResponseEntity<List<ClanDTO>> getTopClans() {
        return ResponseEntity.ok(clanService.getTopClans());
    }

    @GetMapping("/{clanId}/members")
    public ResponseEntity<List<ClanMemberDTO>> getClanMembers(@PathVariable int clanId) {
        return ResponseEntity.ok(clanService.getClanMembers(clanId));
    }

    @GetMapping("/{clanId}/contributions")
    public ResponseEntity<List<ClanMemberContributionDTO>> getMemberContributions(@PathVariable int clanId) {
        return ResponseEntity.ok(clanService.getMemberContributions(clanId));
    }

    // ── Membership ──────────────────────────────────────────────────────────────

    @PostMapping("/{clanId}/join")
    public ResponseEntity<Void> joinClan(@PathVariable int clanId,
                                         @RequestParam UUID userId) {
        clanService.joinClan(clanId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clanId}/leave")
    public ResponseEntity<Void> leaveClan(@PathVariable int clanId,
                                          @RequestParam int userId) {
        clanService.leaveClan(clanId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clanId}/kick")
    public ResponseEntity<Void> kickMember(@PathVariable int clanId,
                                           @RequestParam UUID targetUserId,
                                           @AuthenticationPrincipal User requester) {
        clanService.kickMember(clanId, targetUserId, requester);
        return ResponseEntity.ok().build();
    }

    // ── Invites ─────────────────────────────────────────────────────────────────

    @PostMapping("/{clanId}/invite")
    public ResponseEntity<Void> inviteMember(@PathVariable int clanId,
                                             @RequestParam UUID targetUserId,
                                             @RequestParam UUID requesterId) {
        clanService.inviteMember(clanId, targetUserId, requesterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{clanId}/invite/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable int clanId,
                                             @RequestParam UUID requesterId,
                                             @RequestParam int targetId) {
        clanService.acceptInvite(clanId, requesterId, targetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{clanId}/invite/decline")
    public ResponseEntity<Void> declineInvite(@PathVariable int clanId,
                                              @RequestParam UUID requesterId,
                                              @RequestParam int targetId) {
        clanService.declineInvite(clanId, requesterId, targetId);
        return ResponseEntity.ok().build();
    }

    // ── Roles ───────────────────────────────────────────────────────────────────

    @PatchMapping("/{clanId}/members/promote")
    public ResponseEntity<Void> promoteMember(@PathVariable int clanId,
                                              @RequestParam int userAccepting,
                                              @RequestParam UUID requesterId) {
        clanService.promoteMember(clanId, userAccepting, requesterId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clanId}/members/demote")
    public ResponseEntity<Void> demoteMember(@PathVariable int clanId,
                                             @RequestParam int targetUserId,
                                             @RequestParam UUID requesterId) {
        clanService.demoteMember(clanId, targetUserId, requesterId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clanId}/leadership/transfer")
    public ResponseEntity<Void> transferLeadership(@PathVariable int clanId,
                                                   @RequestParam UUID newLeaderUserId,
                                                   @RequestParam int currentLeaderId) {
        clanService.transferLeadership(clanId, newLeaderUserId, currentLeaderId);
        return ResponseEntity.ok().build();
    }
}
