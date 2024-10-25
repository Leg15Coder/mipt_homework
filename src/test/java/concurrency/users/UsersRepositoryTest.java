package concurrency.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersRepositoryTest {

  @Test
  void findByMsisdn() {
    UsersRepository usersRepository = new UsersRepository();
    usersRepository.updateUserByMsisdn("89201054185", new User("dima", "ryaz"));
    usersRepository.updateUserByMsisdn("88005553535", new User("Anton", "sosed"));
    usersRepository.updateUserByMsisdn("87776662660", new User("Matvey", "the best"));

    assertEquals(usersRepository.findByMsisdn("89201054185").getFirstName(), "dima");
    assertThrows(IllegalArgumentException.class, () -> usersRepository.findByMsisdn(null));
  }

  @Test
  void updateUserByMsisdn() {
    UsersRepository usersRepository = new UsersRepository();
    usersRepository.updateUserByMsisdn("89201054185", new User("dima", "ryaz"));
    usersRepository.updateUserByMsisdn("88005553535", new User("Anton", "sosed"));
    usersRepository.updateUserByMsisdn("87776662660", new User("Matvey", "the best"));

    assertThrows(IllegalArgumentException.class, () -> usersRepository.updateUserByMsisdn(null, null));
    assertThrows(IllegalArgumentException.class, () -> usersRepository.updateUserByMsisdn("8005553535", null));
    assertThrows(IllegalArgumentException.class, () -> usersRepository.updateUserByMsisdn(null, new User("name", "last")));
  }
}