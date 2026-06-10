package com.strongBeton.strongBeton.service.clan;

import com.strongBeton.strongBeton.dto.clan.ClanDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberContributionDTO;
import com.strongBeton.strongBeton.dto.clan.ClanMemberDTO;
import com.strongBeton.strongBeton.dao.ClanMemberContributionRepository;
import com.strongBeton.strongBeton.dao.ClanMembersRepository;
import com.strongBeton.strongBeton.dao.ClanRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.clan.Clan;
import com.strongBeton.strongBeton.entity.clan.ClanMember;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.enums.ClanLeague;
import com.strongBeton.strongBeton.enums.ClanRoleType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClanServiceImpl implements ClanService{

    private ClanRepository clanRepository;
    private ClanMembersRepository clanMembersRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ClanServiceImpl(ClanRepository clanRepository, ClanMembersRepository clanMembersRepository,
                            UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.clanRepository = clanRepository;
        this.clanMembersRepository = clanMembersRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ClanDTO getMyClan(User user) {
        ClanMember clanMember = this.clanMembersRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User is not in a clan"));

        Clan clan = clanMember.getClan();

        if (clan == null) {
            throw new EntityNotFoundException("No clan");
        }

        ClanDTO clanDTO = new ClanDTO();

        clanDTO.setId(clan.getId());
        clanDTO.setName(clan.getName());
        clanDTO.setDescription(clan.getDescription());
        clanDTO.setLogoUrl(clan.getLogoUrl());
        clanDTO.setTotalXP(clan.getTotalXP());
        clanDTO.setClanPoints(clan.getClanPoints());
        clanDTO.setInvite(clan.isInvite());
        clanDTO.setCreatedAt(clan.getCreatedAt());
        List<ClanMemberDTO> clanMemberDTOS = new ArrayList<>();
        for (ClanMember tempClanMember : clan.getMembers()) {
            ClanMemberDTO clanMemberDTO = new ClanMemberDTO();

            clanMemberDTO.setId(tempClanMember.getId());

            if (tempClanMember.getUser() != null) {
                String username = tempClanMember.getUser().getUsername();

                if (username != null && !username.isBlank()) {
                    clanMemberDTO.setUsername(username);
                } else {
                    clanMemberDTO.setUsername(tempClanMember.getUser().getEmail());
                }
            } else {
                clanMemberDTO.setUsername("Athlete");
            }

            clanMemberDTO.setClanId(tempClanMember.getClan().getId());
            clanMemberDTO.setClanRoleType(tempClanMember.getClanRoleType().getText());
            clanMemberDTO.setPoints(tempClanMember.getPoints());
            clanMemberDTO.setJoinedAt(tempClanMember.getJoinedAt());

            clanMemberDTOS.add(clanMemberDTO);
        }
        clanDTO.setMembers(clanMemberDTOS);
        clanDTO.setClanPoints(clan.getClanPoints());
        clanDTO.setCurrLeague(
                clan.getCurrLeague() != null
                        ? clan.getCurrLeague().getText()
                        : "Unranked"
        );

        return clanDTO;
    }
    @Override
    @Transactional
    public ClanDTO createClan(ClanDTO clanDTO, User user) {
        Optional<Clan> result = clanRepository.findByName(clanDTO.getName());

        if (result.isPresent()) {
            throw new IllegalStateException("Clan with this name already exists");
        }

        User managedUser = userRepository.findByUuid(user.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Clan clan = new Clan();
        clan.setName(clanDTO.getName());
        clan.setInvite(clanDTO.isInvite());
        clan.setDescription(clanDTO.getDescription());
        clan.setLogoUrl(clanDTO.getLogoUrl());
        clan.setTotalXP(0);
        clan.setCurrLeague(ClanLeague.UNRANKED);
        clan.setClanPoints(0);
        clan.setClanLevel(1);
        clan.setCreatedAt(LocalDateTime.now());
        clan.setUpdatedAt(LocalDateTime.now());

        Clan savedClan = clanRepository.save(clan);

        ClanMember clanMember = new ClanMember();
        clanMember.setClan(savedClan);
        clanMember.setUser(managedUser);
        clanMember.setPoints(0);
        clanMember.setClanRoleType(ClanRoleType.LEADER);
        clanMember.setJoinderAt(LocalDateTime.now());
        clanMember.setUpdatedAt(LocalDateTime.now());

        ClanMember savedMember = clanMembersRepository.save(clanMember);

        savedClan.setMembers(List.of(savedMember));

        return getMyClan(managedUser);
    }

    @Override
    public ClanDTO getClanById(int clanId) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("No clan with this id"));
        return modelMapper.map(clan, ClanDTO.class);
    }

    @Override
    public ClanDTO updateClan(int clanId, UUID userId, ClanDTO clanDTO) {
        Optional<Clan> result = clanRepository.findByName(clanDTO.getName());
        if (result.isPresent()) {
            throw new IllegalStateException("Clan with this name already exists");
        }
        User user = userRepository.findByUuid(userId)
                .orElseThrow(() -> new EntityNotFoundException("No user with this credits"));
        Clan clan = clanRepository.findById(clanId) // ✅ вземаш правилния clan
                .orElseThrow(() -> new EntityNotFoundException("No clan with this id"));
        ClanMember member = clanMembersRepository.findByClanAndUser(clan, user)
                .orElseThrow(() -> new EntityNotFoundException("User is not in this clan"));
        if (member.getClanRoleType() != ClanRoleType.LEADER) {
            throw new IllegalStateException("Only leader can update clan");
        }
        clan.setName(clanDTO.getName());
        clan.setInvite(clanDTO.isInvite());
        clan.setDescription(clanDTO.getDescription());
        clanRepository.save(clan);
        return this.modelMapper.map(clan, ClanDTO.class);
    }

    @Override
    public void deleteClan(int clanId, UUID userId) {

    }

    @Override
    @Transactional
    public void joinClan(int clanId, User user) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        if (clanMembersRepository.countMembersByClanId(clanId) >= 32) {
            throw new IllegalStateException("No more space in this clan!");
        }


        boolean alreadyInAnyClan = clanMembersRepository.findByUserId(user.getId()).isPresent();

        if (alreadyInAnyClan) {
            throw new IllegalStateException("User is already in a clan!");
        }

        ClanMember clanMember = new ClanMember();
        clanMember.setUser(user);
        clanMember.setClan(clan);
        clanMember.setPoints(0);
        clanMember.setJoinderAt(LocalDateTime.now());
        clanMember.setUpdatedAt(LocalDateTime.now());
        clanMember.setClanRoleType(
                clan.isInvite()
                        ? ClanRoleType.PENDING
                        : ClanRoleType.MEMBER
        );

        this.clanMembersRepository.save(clanMember);
    }
    @Override
    @Transactional
    public void leaveClan(int clanId, User user) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember member = this.clanMembersRepository
                .findByClanAndUser(clan, user)
                .orElseThrow(() -> new EntityNotFoundException("User is not in this clan!"));

        if (member.getClanRoleType() == ClanRoleType.LEADER) {
            throw new IllegalStateException("Leader cannot leave! Transfer leadership first.");
        }

        this.clanMembersRepository.delete(member);
    }

    @Override
    public void kickMember(int clanId, UUID targetUserId, User requester) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember requesterMember = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(requester.getUuid()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Requester is not in this clan!"));

        boolean canKick = requesterMember.getClanRoleType() == ClanRoleType.LEADER
                || requesterMember.getClanRoleType() == ClanRoleType.OFFICER;

        if (!canKick) {
            throw new IllegalStateException("Insufficient permissions to kick members!");
        }

        ClanMember target = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(targetUserId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Target member not found in clan!"));

        if (target.getUser().getUuid().equals(requester.getUuid())) {
            throw new IllegalStateException("Cannot kick yourself!");
        }

        if (requesterMember.getClanRoleType() == ClanRoleType.OFFICER
                && (target.getClanRoleType() == ClanRoleType.LEADER
                || target.getClanRoleType() == ClanRoleType.OFFICER)) {
            throw new IllegalStateException("Officers can only kick members below their rank!");
        }

        clan.getMembers().remove(target);
        this.clanRepository.save(clan);
    }

    @Override
    public void inviteMember(int clanId, UUID targetUserId, UUID requesterId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember requester = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(requesterId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Requesting user is not in this clan!"));

        boolean canInvite = requester.getClanRoleType() == ClanRoleType.LEADER
                || requester.getClanRoleType() == ClanRoleType.OFFICER;

        if (!canInvite) {
            throw new IllegalStateException("Insufficient permissions to invite members!");
        }

        if (clan.getMembers().size() >= 32) {
            throw new IllegalStateException("Clan is full!");
        }

        boolean alreadyInClan = clan.getMembers().stream()
                .anyMatch(m -> m.getUser().getUuid().equals(targetUserId));

        if (alreadyInClan) {
            throw new IllegalStateException("User is already in this clan!");
        }

        User target = this.userRepository.findByUuid(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found!"));

        ClanMember newMember = new ClanMember();
        newMember.setClan(clan);
        newMember.setUser(target);
        newMember.setClanRoleType(ClanRoleType.PENDING);
        newMember.setPoints(0);
        newMember.setJoinderAt(LocalDateTime.now());

        clan.getMembers().add(newMember);
        this.clanRepository.save(clan);
    }

    @Override
    @Transactional
    public void acceptInvite(int clanId, UUID requesterId, int targetId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Theres no clan like this!"));
        boolean isAuthenticated = false;
        Set<ClanMember> clanMembersPending = new HashSet<>();
            for(ClanMember clanMember : clan.getMembers() ){
                if((clanMember.getClanRoleType() == ClanRoleType.LEADER
                        || clanMember.getClanRoleType() == ClanRoleType.OFFICER) && clanMember.getUser().getId() == targetId){
                    isAuthenticated = true;
                }

                if(clanMember.getClanRoleType() == ClanRoleType.PENDING && !clanMembersPending.contains(clanMember)){
                    clanMembersPending.add(clanMember);
                }
            }
            if(!isAuthenticated){
                throw new IllegalStateException("Ja JA Nice try buddy get out!");
            }
            for(ClanMember clanMember : clanMembersPending){
                if(clanMember.getUser().getUuid().equals(requesterId)){
                    clanMember.setClanRoleType(ClanRoleType.MEMBER);
                    break;
                }
            }

        this.clanRepository.save(clan);
    }

    @Override
    @Transactional
    public void declineInvite(int clanId, UUID requesterId, int targetId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Theres no clan like this!"));
        boolean isAuthenticated = false;
        Set<ClanMember> clanMembersPending = new HashSet<>();

        for(ClanMember clanMember : clan.getMembers() ){
            if((clanMember.getClanRoleType() == ClanRoleType.LEADER
                    || clanMember.getClanRoleType() == ClanRoleType.OFFICER) && clanMember.getUser().getId() == targetId){
                isAuthenticated = true;
            }

            if(clanMember.getClanRoleType() == ClanRoleType.PENDING && !clanMembersPending.contains(clanMember)){
                    clanMembersPending.add(clanMember);
            }
        }

        if(!isAuthenticated){
            throw new IllegalStateException("Ja JA Nice try buddy get out!");
        }

        for(ClanMember clanMember : clanMembersPending){
            if(clanMember.getUser().getUuid().equals(requesterId)){
                clan.getMembers().remove(clanMember);
                break;
            }
        }
        this.clanRepository.save(clan);
    }

    @Override
    @Transactional
    public void promoteMember(int clanId, int userAccepting, UUID requesterId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember acceptor = clan.getMembers().stream()
                .filter(m -> m.getUser().getId() == userAccepting)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Accepting user is not in this clan!"));

        boolean canPromote = acceptor.getClanRoleType() == ClanRoleType.LEADER
                || acceptor.getClanRoleType() == ClanRoleType.OFFICER;

        if (!canPromote) {
            throw new IllegalStateException("Insufficient permissions to promote members!");
        }

        ClanMember target = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(requesterId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Target member not found in clan!"));

        if (target.getUser().getId() == userAccepting) {
            throw new IllegalStateException("Cannot promote yourself!");
        }

        int nextValue = target.getClanRoleType().getValue() - 1;
        switch (nextValue) {
            case 0 :
                if (acceptor.getClanRoleType() != ClanRoleType.LEADER)
                    throw new IllegalStateException("Only a Leader can promote someone to Leader!");
                target.setClanRoleType(ClanRoleType.LEADER);
            break;
            case 1: target.setClanRoleType(ClanRoleType.OFFICER);
            break;
            case 2: target.setClanRoleType(ClanRoleType.STRENGTHSPECIALIST);
            break;
            case 3: target.setClanRoleType(ClanRoleType.CONSISTENCYSPECIALIST);
            break;
            case 4: target.setClanRoleType(ClanRoleType.MEMBER);
            break;
            default: throw new IllegalStateException("Cannot promote further!");
        }

        this.clanRepository.save(clan);
    }

    @Override
    public void demoteMember(int clanId, int targetUserId, UUID requesterId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember acceptor = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(requesterId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Requesting user is not in this clan!"));

        boolean canDemote = acceptor.getClanRoleType() == ClanRoleType.LEADER
                || acceptor.getClanRoleType() == ClanRoleType.OFFICER;

        if (!canDemote) {
            throw new IllegalStateException("Insufficient permissions to demote members!");
        }

        ClanMember target = clan.getMembers().stream()
                .filter(m -> m.getUser().getId()== targetUserId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Target member not found in clan!"));

        if (target.getUser().getId() == acceptor.getUser().getId()) {
            throw new IllegalStateException("Cannot demote yourself!");
        }

        // OFFICER не може да демотира LEADER или друг OFFICER
        if (acceptor.getClanRoleType() == ClanRoleType.OFFICER &&
                (target.getClanRoleType() == ClanRoleType.LEADER
                        || target.getClanRoleType() == ClanRoleType.OFFICER)) {
            throw new IllegalStateException("Officers can only demote members below their rank!");
        }

        int nextValue = target.getClanRoleType().getValue() + 1;
        switch (nextValue) {
            case 1: target.setClanRoleType(ClanRoleType.OFFICER);
            break;
            case 2: target.setClanRoleType(ClanRoleType.STRENGTHSPECIALIST);
            break;
            case 3: target.setClanRoleType(ClanRoleType.CONSISTENCYSPECIALIST);
            break;
            case 4: target.setClanRoleType(ClanRoleType.MEMBER);
            break;
            case 5: target.setClanRoleType(ClanRoleType.PENDING);
            break;
            default:
                throw new IllegalStateException("Cannot demote further!");
        }

        this.clanRepository.save(clan);
    }

    @Override
    public void transferLeadership(int clanId, UUID newLeaderUserId, int currentLeaderId) {
        Clan clan = this.clanRepository.findById(clanId)
                .orElseThrow(() -> new EntityNotFoundException("Clan not found!"));

        ClanMember currentLeader = clan.getMembers().stream()
                .filter(m -> m.getUser().getId() == currentLeaderId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Current leader not found in clan!"));

        if (currentLeader.getClanRoleType() != ClanRoleType.LEADER) {
            throw new IllegalStateException("Only the current Leader can transfer leadership!");
        }

        ClanMember newLeader = clan.getMembers().stream()
                .filter(m -> m.getUser().getUuid().equals(newLeaderUserId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Target member not found in clan!"));

        if (newLeader.getUser().getId() == currentLeaderId) {
            throw new IllegalStateException("You are already the Leader!");
        }

        if (newLeader.getClanRoleType() == ClanRoleType.PENDING) {
            throw new IllegalStateException("Cannot transfer leadership to a pending member!");
        }

        currentLeader.setClanRoleType(ClanRoleType.OFFICER);
        newLeader.setClanRoleType(ClanRoleType.LEADER);

        this.clanRepository.save(clan);
    }
}
