package concurrency.users;

public class User {
  private final Object lock = new Object();

  private final String firstName;
  private final String lastName;

  String phoneNumber = "";

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setPhoneNumber(String phoneNumber) {
    synchronized (lock) {
      this.phoneNumber = phoneNumber;
    }
  }

  public String getMSISDN() {
    synchronized (lock) {
      return phoneNumber;
    }
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
