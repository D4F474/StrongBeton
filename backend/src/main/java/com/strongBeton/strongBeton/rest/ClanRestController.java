package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.clan.ClanDTO;
import com.strongBeton.strongBeton.dto.clan.ClanLeaderboardDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.clan.ClanContributionServiceImpl;
import com.strongBeton.strongBeton.service.clan.ClanLeaderboardService;
import com.strongBeton.strongBeton.service.clan.ClanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/clans")
public class ClanRestController {

    private final ClanService clanService;
    private final ClanLeaderboardService clanLeaderboardService;
    private final ClanContributionServiceImpl clanContributionService;
    @Autowired
    public ClanRestController(ClanService clanService, ClanLeaderboardService clanLeaderboardService,
                              ClanContributionServiceImpl clanContributionService) {
        this.clanService = clanService;
        this.clanLeaderboardService = clanLeaderboardService;
        this.clanContributionService = clanContributionService;
    }

    @GetMapping("/me")
    public ResponseEntity<ClanDTO> getMyClan(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clanService.getMyClan(user));
    }

    @GetMapping("/{clanId}")
    public ResponseEntity<ClanDTO> getClanById(@PathVariable int clanId) {
        return ResponseEntity.ok(clanService.getClanById(clanId));
    }

    @GetMapping("/top")
    public ResponseEntity<List<ClanLeaderboardDTO>> getTopClans() {
        return ResponseEntity.ok(clanLeaderboardService.getTopClans());
    }

    @PostMapping
    public ResponseEntity<ClanDTO> createClan(@RequestBody ClanDTO clanDTO, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clanService.createClan(clanDTO, user));
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

    // ── Membership ──────────────────────────────────────────────────────────────

    @PostMapping("/{clanId}/join")
    public ResponseEntity<Void> joinClan(@PathVariable int clanId,
                                         @AuthenticationPrincipal User user) {
        clanService.joinClan(clanId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clanId}/leave")
    public ResponseEntity<Void> leaveClan(@PathVariable int clanId, @AuthenticationPrincipal User user) {
        clanService.leaveClan(clanId, user);
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

    @GetMapping("/{clanId}/contributions")
    public ResponseEntity<List<ClanMemberContributionDTO>> getClanContributions(
            @PathVariable int clanId
    ) {
        return ResponseEntity.ok(clanContributionService.getRecentContributions(clanId));
    }
}
