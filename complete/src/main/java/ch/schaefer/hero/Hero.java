package ch.schaefer.hero;

public class Hero {

    private Integer id;
    private String name;

    public Hero(Integer id, String content) {
        this.id = id;
        this.name = content;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
