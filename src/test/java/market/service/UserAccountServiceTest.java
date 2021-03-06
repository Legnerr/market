package market.service;

import market.dao.UserAccountDAO;
import market.domain.UserAccount;
import market.exception.EmailExistsException;
import market.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {
	private static final long ACCOUNT_ID = 50L;
	private static final String ACCOUNT_EMAIL = "email@domain.com";

	@Mock
	private UserAccountDAO userAccountDAO;

	@Captor
	private ArgumentCaptor<UserAccount> userAccountCaptor;

	private UserAccountService userAccountService;
	private UserAccount userAccount;

	@BeforeEach
	public void setUp() {
		userAccount = new UserAccount.Builder()
			.setId(ACCOUNT_ID)
			.setEmail(ACCOUNT_EMAIL)
			.setPassword("password")
			.setName("Name")
			.setActive(true)
			.build();
		userAccountService = new UserAccountServiceImpl(userAccountDAO);
	}

	@Test
	public void findByEmail() {
		when(userAccountDAO.findByEmail(userAccount.getEmail())).thenReturn(userAccount);

		UserAccount retrieved = userAccountService.findByEmail(userAccount.getEmail());

		assertThat(retrieved, equalTo(userAccount));
	}

	@Test
	public void create() throws EmailExistsException {
		when(userAccountDAO.findByEmail(userAccount.getEmail())).thenReturn(null);

		userAccountService.create(userAccount);

		verify(userAccountDAO).save(userAccountCaptor.capture());
		assertThat(userAccountCaptor.getValue(), equalTo(userAccount));
	}
}
