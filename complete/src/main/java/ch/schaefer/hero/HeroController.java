package ch.schaefer.hero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class HeroController {

	private Map<Integer, Hero> heroes = Collections.synchronizedMap(new HashMap<>());
	private AtomicInteger atomicInteger = new AtomicInteger();

	/*
	 * @RequestMapping(method=RequestMethod.GET) public @ResponseBody Hero sayHello(@RequestParam(value="name",
	 * required=false, defaultValue="Stranger") String name) { return new Hero(counter.incrementAndGet(),
	 * String.format(template, name)); }
	 */
	public HeroController() {
		String[] names = new String[] { "Superman", "Supergirl", "Frozone", "Elastigirl", "Flash", "Batman", "Roboin" };
		for (String name : names) {
			Hero hero = new Hero(atomicInteger.incrementAndGet(), name);
			heroes.put(hero.getId(), hero);
		}
	}

	// -------------------Retrieve All Heros--------------------------------------------------------

	@RequestMapping(value = "/hero/", method = RequestMethod.GET)
	public ResponseEntity<List<Hero>> listAllHeros() {
		System.out.println("Fetching all heroes");
		List<Hero> heros = new ArrayList<>(heroes.values());
		return new ResponseEntity<>(heros, HttpStatus.OK);
	}

	// -------------------Retrieve Single Hero--------------------------------------------------------

	@RequestMapping(value = "/hero/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hero> getHero(@PathVariable("id") int id) {
		System.out.println("Fetching hero with id " + id);
		Hero hero = heroes.get(id);
		if (hero == null) {
			System.out.println("Hero with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hero, HttpStatus.OK);
	}

	// -------------------Create a Hero--------------------------------------------------------

	@RequestMapping(value = "/hero/", method = RequestMethod.POST)
	public ResponseEntity<Void> createHero(@RequestBody Hero hero, UriComponentsBuilder ucBuilder) {
		int nextId = atomicInteger.incrementAndGet();
		System.out.println("Creating hero " + hero.getName());
		hero.setId(nextId);
		heroes.put(nextId, hero);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/hero/{id}").buildAndExpand(hero.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a Hero --------------------------------------------------------

	@RequestMapping(value = "/hero/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Hero> updateHero(@PathVariable("id") int id, @RequestBody Hero hero) {
		System.out.println("Updating hero " + id);

		Hero currentHero = heroes.get(id);

		if (currentHero == null) {
			System.out.println("Hero with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		currentHero.setName(hero.getName());

		return new ResponseEntity<>(currentHero, HttpStatus.OK);
	}

	// ------------------- Delete a Hero --------------------------------------------------------

	@RequestMapping(value = "/hero/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Hero> deleteHero(@PathVariable("id") int id) {
		System.out.println("Fetching & Deleting Hero with id " + id);

		Hero hero = heroes.get(id);
		if (hero == null) {
			System.out.println("Unable to delete. Hero with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		heroes.remove(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Heros --------------------------------------------------------

	@RequestMapping(value = "/hero/", method = RequestMethod.DELETE)
	public ResponseEntity<Hero> deleteAllHeros() {
		System.out.println("Deleting All Heros");
		heroes.clear();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
