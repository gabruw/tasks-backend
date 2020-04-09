package br.ce.wcaquino.taskbackend.conrtoller;

import java.time.LocalDate;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import org.junit.Before;

import br.ce.wcaquino.taskbackend.controller.TaskController;
import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {

	@Mock
	private TaskRepo taskRepo;

	@InjectMocks
	private TaskController taskController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void dontSaveTaskWithoutDescription() {
		Task toDo = new Task();
		toDo.setDueDate(LocalDate.now());

		try {
			taskController.save(toDo);
			Assert.fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
	}

	@Test
	public void dontSaveTaskWithoutDate() {
		Task toDo = new Task();
		toDo.setTask("Descricao");

		try {
			taskController.save(toDo);
			Assert.fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}

	@Test
	public void dontSaveTaskWithoutPastDate() {
		Task toDo = new Task();
		toDo.setTask("Descricao");
		toDo.setDueDate(LocalDate.of(2010, 01, 01));

		try {
			taskController.save(toDo);
			Assert.fail("Não deveria chegar nesse ponto!");
		} catch (ValidationException e) {
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}

	@Test
	public void dontSaveTaskWithSuccess() throws ValidationException {
		Task toDo = new Task();
		toDo.setTask("Descricao");
		toDo.setDueDate(LocalDate.now());

		taskController.save(toDo);
		Mockito.verify(taskRepo).save(toDo);
	}
}
