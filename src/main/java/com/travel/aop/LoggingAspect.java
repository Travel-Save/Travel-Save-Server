package com.travel.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

//@Component
@Aspect
public class LoggingAspect {

	private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Around(value = "execution(* com.travel.board.model.mapper.Board*.*(..))")
	public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug("around call method : {} ", joinPoint.getSignature());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object proceed = joinPoint.proceed();

		stopWatch.stop();

		logger.debug("summary : {}", stopWatch.shortSummary());
		logger.debug("totalTime : {}", stopWatch.getTotalTimeMillis());
		logger.debug("pretty : {}", stopWatch.prettyPrint());

		return proceed;
	}
}