package io.mosip.registration.processor.packet.scanner.job.impl.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler class for executing the jobs
 *
 * @author M1030448
 *
 */

@RefreshScope
@Component
@EnableScheduling
public class PacketScannerBatchJobScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(PacketScannerBatchJobScheduler.class);

	private static final String LOGDISPLAY = "{} - {} - {}";

	private static final String JOB_STATUS = "Job's status";

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job landingZoneScannerJob;

	@Autowired
	private Job virusScannerJob;

	@Autowired
	private Job ftpScannerJob;

	/**
	 * landingZoneScannerJobScheduler runs the landingZoneScannerJob as per given
	 * cron schedule
	 */
	@Scheduled(cron = "${registration.processor.landingzone.cron.job.schedule}")
	public void landingZoneScannerJobScheduler() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(landingZoneScannerJob, jobParameters);

			LOGGER.info(LOGDISPLAY, JOB_STATUS, jobExecution.getId(), jobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			LOGGER.error(LOGDISPLAY, "landingZoneScannerJobScheduler failed to execute", e);
		}
	}

	/**
	 * virusScannerJobScheduler runs the virusScannerJob as per given cron schedule
	 */
	@Scheduled(cron = "${registration.processor.virusscan.cron.job.schedule}")
	public void virusScannerJobScheduler() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(virusScannerJob, jobParameters);
			LOGGER.info(LOGDISPLAY, JOB_STATUS, jobExecution.getId(), jobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			LOGGER.error(LOGDISPLAY, "virusScannerJobScheduler failed to execute", e);
		}
	}

	/**
	 * ftpJobScheduler runs the ftpJobScheduler as per given cron schedule
	 */
	@Scheduled(cron = "${registration.processor.ftp.cron.job.schedule}")
	public void ftpJobScheduler() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();

		try {
			JobExecution jobExecution = jobLauncher.run(ftpScannerJob, jobParameters);
			LOGGER.info(LOGDISPLAY, JOB_STATUS, jobExecution.getId(), jobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			LOGGER.error(LOGDISPLAY, "ftpJobScheduler failed to execute", e);
		}
	}

}
