package dev.keeneye.services;

import dev.keeneye.dto.GroupRequest;
import dev.keeneye.dto.GroupDto;
import dev.keeneye.dto.GroupsPage;
import dev.keeneye.dto.ShortGroupInfo;
import dev.keeneye.entities.Group;
import dev.keeneye.entities.Professor;
import dev.keeneye.entities.Student;
import dev.keeneye.exceptions.GroupNotEmptyBeforeDeletionException;
import dev.keeneye.exceptions.ResourceNotFoundException;
import dev.keeneye.mappers.GroupMapper;
import dev.keeneye.repositories.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupsPage getAllGroupsPage(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset / limit, limit, Sort.by("id"));
        Slice<GroupDto> groupSlice = groupRepository.findAllBy(pageRequest).map(groupMapper::toDto);
        return GroupsPage.from(groupSlice);
    }

    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findByIdFetch(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        return groupMapper.toDto(group);
    }
    public GroupDto createGroup(GroupRequest request) {
        Group group = new Group();
        group.setName(request.name());
        groupRepository.save(group);
        return groupMapper.toDto(group);
    }


    public ShortGroupInfo updateGroupName(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        group.setName(request.name());
        groupRepository.save(group);
        return groupMapper.toGroupInfo(group);
    }

    @Transactional
    public void deleteEmptyGroup(Long id) {
        Group group = groupRepository.findByIdFetch(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<Professor> professors = group.getProfessors();
        if (professors != null && !professors.isEmpty())
            throw new GroupNotEmptyBeforeDeletionException("Group not empty: has professors");
        List<Student> students = group.getStudents();
        if (students != null && !students.isEmpty()) {
            throw new GroupNotEmptyBeforeDeletionException("Group not empty: has students");
        }

        groupRepository.delete(group);
    }
}
