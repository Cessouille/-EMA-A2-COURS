package bankAccountApp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {

	static String name = "george";
	static char gender = 'M';
	static int age = 62;
	static float height = 5.9f;
	static float weight = 160f;
	static String hairColor = "brown";
	static String eyeColor = "hazel";
	static String emailAddress = "bozo.net";
	String serializedPerson = null;

	@Before
	public void setup() {
		name = "george";
		gender = 'M';
		age = 62;
		height = 5.9f;
		weight = 160f;
		hairColor = "brown";
		eyeColor = "hazel";
		emailAddress = "bozo.net";
		serializedPerson = name + Person.DELIM + gender + Person.DELIM + age + Person.DELIM + height + Person.DELIM
				+ weight + Person.DELIM + hairColor + Person.DELIM + eyeColor + Person.DELIM + emailAddress;
	}

	@Test
	public void test_create_ver1_and_gets() {
		// Given
		Person person = new Person();

		// Then
		assertEquals("", person.getName());
		assertEquals('M', person.getGender());
		assertEquals(0, person.getAge());
		assertEquals(0.0, person.getHeight(), 0);
		assertEquals(0.0, person.getWeight(), 0);
		assertEquals("", person.getHairColor());
		assertEquals("", person.getEyeColor());
	}

	@Test
	public void test_create_ver1_and_gets_hamcrest() {
		// Given
		Person person = new Person();

		// Then
		assertThat(person.getName(), equalTo(""));
		assertThat(person.getAge(), equalTo(0));
		assertThat(person.getGender(), equalTo('M'));
		assertThat(person.getHeight(), equalTo(0.0f));
		assertThat(person.getWeight(), equalTo(0.0f));
		assertThat(person.getHairColor(), equalTo(""));
		assertThat(person.getEyeColor(), equalTo(""));

	}

	@Test
	public void test_create_ver2_and_gets() {
		// Given
		Person person = new Person(name, gender, age, height);

		// Then
		assertEquals(name, person.getName());
		assertEquals(gender, person.getGender());
		assertEquals(age, person.getAge());
		assertEquals(height, person.getHeight(), 0);

	}

	@Test
	public void test_create_ver2_and_gets_hamcrest() {
		// Given
		Person person = new Person(name, gender, age, height);
		assertThat(person.getName(), equalTo(name));
		assertThat(person.getGender(), equalTo(gender));
		assertThat(person.getAge(), equalTo(age));
		assertThat(person.getHeight(), equalTo(height));
	}

	@Test
	public void test_create_ver3_and_gets() throws Exception {
		// Given
		Person person = new Person(name, gender, age, weight, height, hairColor, eyeColor, emailAddress);

		// Then
		assertEquals(name, person.getName());
		assertEquals(gender, person.getGender());
		assertEquals(age, person.getAge());
		assertEquals(height, person.getHeight(), 0f);
		assertEquals(weight, person.getWeight(), 0f);
		assertEquals(hairColor, person.getHairColor());
		assertEquals(eyeColor, person.getEyeColor());
		assertEquals(emailAddress, person.getEmail());
	}

	@Test
	public void test_create_ver3_and_gets_hamcrest() throws Exception {
		// Given
		Person person = new Person(name, gender, age, weight, height, hairColor, eyeColor, emailAddress);

		// Then
		assertThat(person.getName(), equalTo(name));
		assertThat(person.getGender(), equalTo(gender));
		assertThat(person.getHeight(), equalTo(height));
		assertThat(person.getAge(), equalTo(age));
		assertThat(person.getWeight(), equalTo(weight));
		assertThat(person.getHairColor(), equalTo(hairColor));
		assertThat(person.getEyeColor(), equalTo(eyeColor));
		assertThat(person.getEmail(), equalTo(emailAddress));

	}

	@Test
	public void test_create_ver4_and_gets() throws Exception {
		// Given
		Person person = new Person(serializedPerson);

		// Then
		assertEquals(name, person.getName());
		assertEquals(gender, person.getGender());
		assertEquals(age, person.getAge());
		assertEquals(height, person.getHeight(), 0f);
		assertEquals(weight, person.getWeight(), 0f);
		assertEquals(hairColor, person.getHairColor());
		assertEquals(eyeColor, person.getEyeColor());
		assertEquals(emailAddress, person.getEmail());
		assertEquals(name, person.getName());

	}

	@Test
	public void test_create_ver4_and_gets_hamcrest() throws Exception {
		// Given
		Person person = new Person(serializedPerson);

		// Then
		assertThat(person.getHeight(), equalTo(height));
		assertThat(person.getHeight(), equalTo(height));
		assertThat(person.getAge(), equalTo(age));
		assertThat(person.getEmail(), equalTo(emailAddress));
		assertThat(person.getName(), equalTo(name));
		assertThat(person.getEyeColor(), equalTo(eyeColor));
		assertThat(person.getWeight(), equalTo(weight));
		assertThat(person.getGender(), equalTo(gender));
		assertThat(person.getHairColor(), equalTo(hairColor));
	}

	@Test
	public void test_all_sets_hamcrest() throws Exception {
		String newName = "Peter";
		char newGender = 'F';
		int newAge = 63;
		float newHeight = 6f;
		float newWeight = 170f;
		String newEyeColor = "green";
		String newEmailAddress = "gene@homeontherange.com";

		// TODO add test setHairColor

		// Given
		Person person = new Person(name, gender, age, weight, height, hairColor, eyeColor, emailAddress);

		// When
		person.setAge(newAge);
		person.setEmail(newEmailAddress);
		person.setName(newName);
		person.setHeight(newHeight);
		person.setEyeColor(newEyeColor);
		person.setWeight(newWeight);
		person.setGender(newGender);

		// Then
		assertThat(person.getAge(), equalTo(newAge));
		assertThat(person.getHeight(), equalTo(newHeight));
		assertThat(person.getEmail(), equalTo(newEmailAddress));
		assertThat(person.getName(), equalTo(newName));
		assertThat(person.getEyeColor(), equalTo(newEyeColor));
		assertThat(person.getWeight(), equalTo(newWeight));
		assertThat(person.getGender(), equalTo(newGender));
	}

	@Test
	public void test_person_serialization_hamcrest() throws Exception {
		// Given
		Person person = new Person(name, gender, age, weight, height, hairColor, eyeColor, emailAddress);
		String serializedPerson = person.toString();

		// Then
		String[] tokens = serializedPerson.split(Person.DELIM);

		assertThat(8, equalTo(tokens.length));
		assertThat(tokens[0], equalTo(name));
		assertThat(tokens[1].charAt(0), equalTo(gender));
		assertThat(Integer.valueOf(tokens[2]), equalTo(age));
		assertThat(Float.valueOf(tokens[3]), equalTo(height));
		assertThat(Float.valueOf(tokens[4]), equalTo(weight));
		assertThat(tokens[5], equalTo(hairColor));
		assertThat(tokens[6], equalTo(eyeColor));
		assertThat(tokens[7], equalTo(emailAddress));

	}

	@Test(expected = Exception.class)
	public void test_person_validate_gender() throws Exception {
		char newGender = 'l';
		Person person = new Person(name, gender, age, weight, height, hairColor, eyeColor, emailAddress);
		person.setGender(newGender);
	}

	@SuppressWarnings("unused")
	@Test(expected = Exception.class)
	public void test_person_validate_gender_by_constructor() throws Exception {
		char newGender = 'l';
		// constructor will generate an exception
		Person person = new Person(name, newGender, age, weight, height, hairColor, eyeColor, emailAddress);
	}

}
