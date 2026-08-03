// ================= TEST KAFKA M KHOLIL RAHIMULLAH=================
import kafka.KafkaHelperCLI



String containerName = 'kafka-local-kafka-1'  
String topic = 'test-topic'
int timeoutMs = 15000
int maxMessages = 1

List<String> messages = KafkaHelperCLI.consumeMessagesViaCLI(containerName, topic, timeoutMs, maxMessages)

assert messages.size() > 0 : 'Tidak ada pesan yang diterima dari topic Kafka dalam batas waktu yang ditentukan'

println 'Total pesan diterima: ' + messages.size()
messages.each { msg -> println 'Isi pesan: ' + msg }

println 'KAFKA CONSUMER TEST (CLI) OK'
