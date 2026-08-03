package kafka

import com.kms.katalon.core.annotation.Keyword

/**
 * Alternatif TANPA library Kafka Java (kafka-clients.jar).
 * Cara kerja: menjalankan perintah "docker exec ... kafka-console-consumer"
 * lewat command line, lalu menangkap output-nya.
 *
 * SYARAT:
 * - Docker & docker-compose sudah jalan (lihat kafka-local/docker-compose.yml)
 * - Container Kafka bernama sesuai docker-compose (default: kafka-local-kafka-1)
 *   Cek nama sebenarnya dengan: docker ps
 */
class KafkaHelperCLI {

	@Keyword
	static List<String> consumeMessagesViaCLI(String containerName, String topic, int timeoutMs, int maxMessages) {
		// Perintah kafka-console-consumer bawaan image Kafka, baca dari awal topic, berhenti otomatis setelah timeout
		String cmd = "docker exec ${containerName} kafka-console-consumer " +
				"--bootstrap-server localhost:9092 " +
				"--topic ${topic} " +
				"--from-beginning " +
				"--max-messages ${maxMessages} " +
				"--timeout-ms ${timeoutMs}"

		println "Menjalankan perintah: ${cmd}"

		Process process = ['bash', '-c', cmd].execute()
		String output = process.text  // menunggu proses selesai lalu ambil semua output
		process.waitFor()

		List<String> messages = output.readLines().findAll { line ->
			!line.trim().isEmpty() && !line.startsWith('[')  // buang baris log/error
		}

		println "Pesan diterima (${messages.size()}): ${messages}"
		return messages
	}
}
