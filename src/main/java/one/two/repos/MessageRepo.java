package one.two.repos;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import one.two.domain.Message;
public interface MessageRepo extends CrudRepository<Message, Integer>{
	List<Message> findByTag(String tag);

}
