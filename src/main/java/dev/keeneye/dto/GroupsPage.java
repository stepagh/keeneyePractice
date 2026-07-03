package dev.keeneye.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record GroupsPage(
        List<GroupDto> groups,
        boolean hasMore
        )
{
    public static GroupsPage from(Slice<GroupDto> groupSlice) {
        return new GroupsPage(groupSlice.getContent(), groupSlice.hasNext());
    }
}
