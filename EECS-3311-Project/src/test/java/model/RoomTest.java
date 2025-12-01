package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjfx.EECS_3311_Project.model.Room;

class RoomTest {

	String roomId;
	String buildingName;
	String roomName;
	int capacity;
	boolean isActive;

	@BeforeEach
	void setup() {
		roomId = UUID.randomUUID().toString();
		buildingName = "Building A";
		roomName = "Room 101";
		capacity = 5;
		isActive = false; // constructors ignore this and force true
	}

	@Test
	void constructor_with_valid_params_and_room_id() {
		Room room = new Room(roomId, buildingName, roomName, capacity, isActive);

		assertEquals(roomId, room.getRoomId());
		assertEquals(buildingName, room.getBuildingName());
		assertEquals(roomName, room.getRoomName());
		assertEquals(capacity, room.getCapacity());

		// constructor forces isActive = true regardless of param
		assertTrue(room.isActive());
	}

	@Test
	void constructor_without_room_id_generates_id() {
		Room room = new Room(buildingName, roomName, capacity, isActive);

		assertNotNull(room.getRoomId());
		assertEquals(buildingName, room.getBuildingName());
		assertEquals(roomName, room.getRoomName());
		assertEquals(capacity, room.getCapacity());

		// constructor forces isActive = true regardless of param
		assertTrue(room.isActive());
	}

	@Test
	void csv_constructor_with_valid_param() {
		String csv = String.join(",",
				roomId,               // id
				roomName,             // roomName
				"true",               // isActive
				String.valueOf(capacity),
				buildingName
		);

		Room room = new Room(csv);

		assertEquals(roomId, room.getRoomId());
		assertEquals(roomName, room.getRoomName());
		assertTrue(room.isActive());
		assertEquals(capacity, room.getCapacity());
		assertEquals(buildingName, room.getBuildingName());
	}

	@Test
	void room_to_csv_row() {
		Room room = new Room(roomId, buildingName, roomName, capacity, isActive);
		// ensure state matches what we expect in CSV
		room.setActive(true);
		room.setCapacity(capacity);
		room.setBuildingName(buildingName);
		room.setRoomName(roomName);
		room.setRoomId(roomId);

		String expected = String.join(",",
				roomId,
				roomName,
				"true",
				String.valueOf(capacity),
				buildingName
		);

		assertEquals(expected, room.toCSVRow());
	}

	@Test
	void set_occupancy_with_negative_value_sets_zero() {
		Room room = new Room(roomId, buildingName, roomName, capacity, isActive);

		room.setOccupancy(-5);

		assertEquals(0, room.getOccupancy());
	}

	@Test
	void update_with_enter_event_increments_occupancy_until_capacity() {
		Room room = new Room(roomId, buildingName, roomName, capacity, isActive);

		// sensor field is null initially, so passing null sensor will still update
		for (int i = 0; i < capacity + 3; i++) {
			room.update(null, "enter");
		}

		assertEquals(capacity, room.getOccupancy());
	}

	@Test
	void update_with_exit_event_decrements_occupancy_but_not_below_zero() {
		Room room = new Room(roomId, buildingName, roomName, capacity, isActive);

		// bump up occupancy a bit
		room.setOccupancy(2);

		room.update(null, "exit");
		assertEquals(1, room.getOccupancy());

		room.update(null, "exit");
		assertEquals(0, room.getOccupancy());

		// extra exits shouldn't go negative
		room.update(null, "exit");
		room.update(null, "exit");
		assertEquals(0, room.getOccupancy());
	}
}
