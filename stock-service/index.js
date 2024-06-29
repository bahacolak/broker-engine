const rabbit = require('amqplib')

let STOCK_NUMBER = 10

const ORDER_QUEUE_NAME = 'orderQ'
const RESULT_QUEUE_NAME = 'resultQ'

connection = rabbit.connect('amqp://localhost')
connection.then(async (conn) => {
    const channel = await conn.createChannel()
    await channel.assertQueue(ORDER_QUEUE_NAME)
    await channel.assertQueue(RESULT_QUEUE_NAME)

    const orderTimeouts = {}

    const sendMsg = msgObj => {
        const msgString = JSON.stringify(msgObj)
        console.log("YOLLADIM");
        channel.sendToQueue(RESULT_QUEUE_NAME, Buffer.from(msgString))
    }

    channel.consume(ORDER_QUEUE_NAME, m => {
        const msgJsonString = m.content.toString()
        const msgJson = JSON.parse(msgJsonString)
        console.log(msgJson);
        const { orderId, orderType, numberOfShares } = msgJson

        if (!orderType) return

        if(orderType == "buy" && numberOfShares > STOCK_NUMBER) {
            sendMsg({ orderId, orderType, status: "failed" })
        } else if(orderType) {
            let timeout

            switch(orderType) {
                case "buy":
                    timeout = setTimeout(() => {
                        sendMsg({ orderId, orderType, status: "done" })
                        STOCK_NUMBER -= numberOfShares
                        return
                    }, 10000)
                    orderTimeouts[orderId] = timeout
                    break;
                case "sell":
                    timeout = setTimeout(() => {
                        sendMsg({ orderId, orderType, status: "done" })
                        STOCK_NUMBER += numberOfShares
                        return
                    }, 10000)
                    orderTimeouts[orderId] = timeout
                    break;
                case "cancel":
                    clearTimeout(orderTimeouts[orderId])
                    break;
            }
        }

        channel.ack(m)
    })
})