package concurrency.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UsersRepository implements UserRepository {
  private final List<User> data = Collections.synchronizedList(new ArrayList<>());

  @Override
  public User findByMsisdn(String msisdn) {
    if (msisdn == null) {
      throw new IllegalArgumentException("msisdn не может быть null");
    }
    synchronized (data) {
      for (User current : data) {
        if (Objects.equals(current.getMSISDN(), msisdn)) {
          return current;
        }
      }
      return null;
    }
  }

  @Override
  public synchronized void updateUserByMsisdn(String msisdn, User user) {
    if (msisdn == null) {
      throw new IllegalArgumentException("msisdn не может быть null");
    }
    if (user == null) {
      throw new IllegalArgumentException("user не может быть null");
    }
    synchronized (data) {
      for (User current : data) {
        if (Objects.equals(current.getFirstName(), user.getFirstName()) && Objects.equals(current.getLastName(), user.getLastName())) {
          this.data.remove(current);
          current.setPhoneNumber(msisdn);
          this.data.add(current);
          break;
        }
      }
      user.setPhoneNumber(msisdn);
      this.data.add(user);
    }
  }
}
