var mqtt = require('mqtt')
var axios = require('axios')

const main = async ()=>{
    const res = await axios({
        method: 'POST',
        url: `http://a1.easemob.com/1128210519085787/fangfa2/token`,
        data: JSON.stringify({
            grant_type: "password",
            username: "mqttuser",
            password:"mqttuser"
        }, null, 2),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    console.log("res.data", res.data);
    console.log("==========")

    var client  = mqtt.connect('mqtts://0ljii0.cn1.mqtt.chat:1884', {
        protocolVersion: 5,
        clientId: "mqttuser@0ljii0",
        username: "mqttuser",
        password: res.data.access_token
    })
    client.on('connect', function () {
        console.log("Connected");
        const topic = '/agora/upstream/#'
        client.subscribe(topic, function (err) {
            if (err){
                console.error(err);
            }
            console.log("Subscribed to topic ", topic);
        })
    })
    client.on('error', function(err){
        console.error(err)
    })

    client.on('message', function (topic, message) {
        // message is Buffer
        console.log("=============")
        console.log("TOPIC: ", topic);
        console.log(message.toString())
    })
}

main()