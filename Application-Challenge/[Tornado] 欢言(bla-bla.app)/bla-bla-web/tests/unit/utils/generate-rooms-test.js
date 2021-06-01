import generateRooms from 'bla-bla-web/utils/generate-rooms';
import { module, test } from 'qunit';

module('Unit | Utility | generate-rooms', function () {
  test('generate default rooms', function (assert) {
    const room = generateRooms({ isGetRandomAttendeeCount: false });

    assert.ok(Array.isArray(room.data));
    assert.equal(room.data.length, 1);
    assert.ok(Array.isArray(room.included));
    assert.equal(room.included.length, 2);
  });

  test('generate a single room', function (assert) {
    const room = generateRooms({
      isGenerateSingleRoom: true,
      isGetRandomAttendeeCount: false,
    });

    assert.equal(typeof room.data, 'object');
    assert.ok(Array.isArray(room.included));
    assert.equal(room.included.length, 2);
  });

  test('generate expected rooms', function (assert) {
    const room = generateRooms({
      roomCount: 3,
      isGetRandomAttendeeCount: false,
    });
    assert.ok(Array.isArray(room.data));
    assert.equal(room.data.length, 3);
  });

  test('generate expected attendees', function (assert) {
    const room = generateRooms({
      attendeeCount: 10,
      isGetRandomAttendeeCount: false,
    });
    assert.ok(Array.isArray(room.data));
    assert.equal(room.data.length, 1);
    assert.ok(Array.isArray(room.included));
    assert.equal(room.included.length, 10);
  });
});
