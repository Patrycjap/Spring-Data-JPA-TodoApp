package pp.springdata.todo;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) Category category,
                       @RequestParam(required = false, defaultValue = "false") Boolean archive) {
        List<Todo> todoList;
        if (category != null) {
            todoList = todoRepository.findByCategoryOrderByDeadline(category);
        } else {
            todoList = todoRepository.findAll(Sort.by("deadline"));
        }

        model.addAttribute("todoList", todoList.stream()
                .filter(todo -> todo.getDone().equals(archive))
                .collect(Collectors.toList()));
        model.addAttribute("todoAdd", new Todo());
        return "home";
    }

    @GetMapping("/todo/{id}")
    public String showToDo(@PathVariable Long id, Model model) {
        Optional<Todo> todoOptional = todoRepository.findById(id);

        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            model.addAttribute("todo", todo);
            return "todo";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/todo/{id}/edit")
    public String todoEditForm(@PathVariable Long id, Model model) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            model.addAttribute("todoToEdit", todo);
            return "todoEdit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/todo/{id}/edit")
    public String editTask(@PathVariable Long id, Todo todo) {
        Todo todo1 = todoRepository.findById(id).orElseThrow();
        todo1.setTitle(todo.getTitle());
        todo1.setDescription(todo.getDescription());
        todo1.setDeadline(todo.getDeadline());
        todo1.setCategory(todo.getCategory());
        todo1.setDone(todo.getDone());
        todoRepository.save(todo1);
        return "redirect:/todo/" + todo1.getId();
    }

    @GetMapping("/todo/add")
    public String todoAddForm(Model model) {
        model.addAttribute("todoAdd", new Todo());
        return "todoAdd";
    }

    @PostMapping("/todo/add")
    public String addTask(Todo todo) {
        todoRepository.save(todo);
        return "redirect:/todo/" + todo.getId();
    }
}