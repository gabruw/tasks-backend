package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
	
	@Test
	public void returnTrueForFutureDates() {
		LocalDate date = LocalDate.of(2030, 01, 01);
		Boolean isFutureDate = DateUtils.isEqualOrFutureDate(date);
		Assert.assertTrue(isFutureDate);
	}
	
	@Test
	public void returnTrueForToday() {
		LocalDate date = LocalDate.now();
		Boolean isActualDate = DateUtils.isEqualOrFutureDate(date);
		Assert.assertTrue(isActualDate);
	}
	
	@Test
	public void returnFalseForPastDates() {
		LocalDate date = LocalDate.of(2010, 01, 01);
		Boolean isPastDate = DateUtils.isEqualOrFutureDate(date);
		Assert.assertFalse(isPastDate);
	}
}
