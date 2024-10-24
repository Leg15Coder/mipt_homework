package concurrency.users;

public class User {
  String firstName;
  String lastName;

  String phoneNumber = "";

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getMSIDN() {
    return phoneNumber;
  }
}
