package concurrency.users;

public interface UserRepository {

  User findByMsisdn(String msisdn);

  void updateUserByMsisdn(String msisdn, User user);
}
