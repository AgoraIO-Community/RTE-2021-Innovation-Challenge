package helper

import (
	"github.com/google/uuid"
	"math/rand"
)

const letterBytes = "abcdefghijklmnopqrstuvwxyz1234567890"

func RandStringBytes(n int) string {
	b := make([]byte, n)
	for i := range b {
		b[i] = letterBytes[rand.Intn(len(letterBytes))]
	}
	return string(b)
}

func GetUserId() string {
	return "u-" + uuid.New().String()
}

func GetNotificationId() string {
	return "n-" + uuid.New().String()
}

func GetReservationId() string {
	return "r-" + uuid.New().String()
}
