package helper

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func SendError(c *gin.Context, responseType string, responseBody ...interface{}) {
	if len(responseBody) > 0 {
		c.AbortWithStatusJSON(http.StatusOK, gin.H{"type": responseType, "data": responseBody[0]})
		return
	}
	c.AbortWithStatusJSON(http.StatusOK, gin.H{"type": responseType})
}

func SendSuccess(c *gin.Context) {
	c.AbortWithStatusJSON(http.StatusOK, gin.H{"type": "Success"})
}

func SendData(c *gin.Context, data interface{}) {
	c.AbortWithStatusJSON(http.StatusOK, gin.H{"type": "Success", "data": data})
}
