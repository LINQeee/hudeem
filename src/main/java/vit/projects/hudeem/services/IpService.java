package vit.projects.hudeem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vit.projects.hudeem.dto.IpDTO;
import vit.projects.hudeem.mappers.IpMapper;
import vit.projects.hudeem.repositories.IpRepository;

@Service

@RequiredArgsConstructor
public class IpService {

    private final IpRepository ipRepository;
    private final IpMapper ipMapper;

    public void saveNewIp(Long userId, String ip) {
        IpDTO ipDTO = new IpDTO(userId, ip);
        ipRepository.save(ipMapper.fromDTO(ipDTO));
    }
}
