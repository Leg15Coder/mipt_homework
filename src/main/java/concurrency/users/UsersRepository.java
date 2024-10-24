package concurrency.users;

import java.util.ArrayList;
import java.util.Objects;

public class UsersRepository implements UserRepository {
  private final ArrayList<User> data = new ArrayList<>();

  @Override
  public User findByMsisdn(String msisdn) {
    for (User current : data) {
      if (Objects.equals(current.getMSIDN(), msisdn)) {
        return current;
      }
    }
    return null;
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    for (User current : data) {
      if (Objects.equals(current.firstName, user.firstName) && Objects.equals(current.lastName, user.lastName)) {
        current.setPhoneNumber(msisdn);
        break;
      }
    }
    user.setPhoneNumber(msisdn);
    this.data.add(user);
  }
}
