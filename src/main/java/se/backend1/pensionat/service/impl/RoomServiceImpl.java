package se.backend1.pensionat.service.impl;

import org.springframework.stereotype.Service;
import se.backend1.pensionat.entity.Room;
import se.backend1.pensionat.model.RoomType;
import se.backend1.pensionat.repository.RoomRepository;
import se.backend1.pensionat.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;

    }
@Override
    public Room saveRoom(Room room){
        //If the room is not a DOUBLE room but still has extra beds set (maxExtraBeds > 0), then that’s not allowed.
        if(room.getRoomType()!=RoomType.DOUBLE&& room.getMaxExtraBeds()>0){
            throw new IllegalArgumentException("Extrasängar är endast tillåtna för dubbelrum");
        }
        return roomRepository.save(room);
}
}
