package bankAccountApp;

import java.util.Scanner;

//TODO Add a validation to email address to insure that it is a string with a "." imbedded and characters to the right an left
//TODO Replace comments with java doc. Type in /** and hit enter above method

/**
 * Shows how to create a simple Person class
 * 
 * @author jay
 */
public class Person {
	// All attributes are declared as private in order to
	// prevent the user from modifying them directly.
	private String name;
	private char gender;
	private int age;
	private float height;
	private float weight;
	private String hairColor;
	private String eyeColor;
	private String email;


	public static final String DELIM = "\n";
	
	/**
	 * Create Person
	 * 
	 * @param newName
	 * @param newGender
	 * @param newAge
	 * @param newHeight
	 */
	public Person(String newName, char newGender, int newAge, float newHeight) {
		name = newName;
		gender = newGender;
		age = newAge;
		height = newHeight;
		weight = 80;
		hairColor = "Black";
		eyeColor = "Blue";
	}

	/*
	 * Create Person
	 */
	public Person() {
		name = "";
		gender = 'M';
		age = 0;
		height = 0;
		weight = 0;
		hairColor = "";
		eyeColor = "";
	}

	/**
	 * Create Person
	 * 
	 * @param newName
	 * @param newGender
	 * @param newAge
	 * @param newWeight
	 * @param newHeight
	 * @param newHairColor
	 * @param newEyeColor
	 * @param newEmail
	 * @throws Exception 
	 */
	public Person(String newName, char newGender, int newAge, float newWeight, float newHeight, String newHairColor,
			String newEyeColor, String newEmail) throws Exception {
		name = newName;
		gender = newGender;
		age = newAge;
		weight = newWeight;
		height = newHeight;
		hairColor = newHairColor;
		eyeColor = newEyeColor;
		email = newEmail;
		validate();
	}

	/**
	 * Create Person
	 * 
	 * @param accountHolder
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	public Person(String accountHolder) throws Exception {
		Scanner scan = null;
		try {
			scan = new Scanner(accountHolder).useDelimiter(DELIM);
			name = scan.next();
			gender = scan.next().charAt(0);
			age = scan.nextInt();
			height = scan.nextFloat();
			weight = scan.nextFloat();
			hairColor = scan.next();
			eyeColor = scan.next();
			email = scan.next();
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		validate();
	}

	// For every private attribute we should have public get and set
	// methods that allow us to read the attribute and write to it.

	/**
	 * Get Name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Name
	 * 
	 * @param newName
	 */
	public void setName(String newName) {
		name = newName;
	}

	// getter and setter for the gender attribute
	/**
	 * Get Gender
	 * 
	 * @return
	 */
	public char getGender() {
		return gender;
	}

	/**
	 * Set Gender
	 * 
	 * @param newGender
	 * @throws Exception 
	 */
	public void setGender(char newGender) throws Exception {
		if (validateGender(newGender)) {
			this.gender = newGender;
		}
	}

	/**
	 * Get Age
	 * 
	 * @return
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Set Age
	 * 
	 * @param newAge
	 */
	public void setAge(int newAge) {
		// Set the internal value of age to the value requested by the user
		// but also prevent the user from setting an invalid age

		// For example all negative age values are invalid
		// So set the age only if its bigger than 0.
		if (newAge >= 0)
			age = newAge;

	}

	/**
	 * Get Height
	 * @return
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Set Height
	 * @param height
	 */
	public void setHeight(float height) {
		// If we name our parameter with the same name
		// as the attribute, we should use the this keyword
		// so that Java knows which one we are talking about.
		// this.height means the height attribute internal to the class.

		this.height = height;
	}

	// getter and setter for the weight attribute
	public float getWeight() {
		return weight;
	}

	public void setWeight(float newWeight) {
		weight = newWeight;
	}

	// getter and setter for the name hair color attribute
	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String newHairColor) {
		if (newHairColor.equalsIgnoreCase("black") || newHairColor.equalsIgnoreCase("brown")
				|| newHairColor.equalsIgnoreCase("blond") || newHairColor.equalsIgnoreCase("white"))
			hairColor = newHairColor;
	}

	// getter and setter for the eye color attribute
	public String getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(String newEyeColor) {
		eyeColor = newEyeColor;
	}

	// All the other class behaviors/methods

	// This method stores all current class information in a string
	// and returns it.
	public String toString() {
		// Store all attribute values in string personInfo
		String personInfo = getName() + DELIM + getGender() + DELIM + getAge() + DELIM + getHeight() + DELIM
				+ getWeight() + DELIM + getHairColor() + DELIM + getEyeColor() + DELIM + getEmail();
		return personInfo;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	private void validate() throws Exception {
		validateGender(gender);
		return;
	}
	
	/**
	 * validate the gender
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private boolean validateGender(char value) throws Exception {
		if ((value == 'M' || (value == 'F')|| (value == 'm') || (value == 'f' )))
			return true;
		else
			throw new Exception("Invalid gender: " + gender);
	}

}
