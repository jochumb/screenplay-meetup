package com.xebia.meetup.screenplay.features.messaging;

import com.xebia.meetup.screenplay.abilities.Authenticate;
import com.xebia.meetup.screenplay.questions.Conversations;
import com.xebia.meetup.screenplay.tasks.BrowseToMessagesPage;
import com.xebia.meetup.screenplay.tasks.LogOut;
import com.xebia.meetup.screenplay.tasks.login.OpenTheLoginPageAndLogin;
import com.xebia.meetup.screenplay.tasks.messaging.DraftANewMessage;
import com.xebia.meetup.screenplay.tasks.messaging.SendTheMessage;
import com.xebia.meetup.utils.Credentials;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static net.serenitybdd.screenplay.EventualConsequence.eventually;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

/**
 * Created by jochum on 15/08/16.
 */
@RunWith(SerenityRunner.class)
public class SendMessageTest {
    Actor anna = Actor.named("Anna");

    @Managed(uniqueSession = true)
    public WebDriver herBrowser;

    @Steps OpenTheLoginPageAndLogin openTheLoginPageAndLogin;
    @Steps BrowseToMessagesPage browseToMessagesPage;
    @Steps SendTheMessage sendTheMessage;
    @Steps LogOut logOut;

    @Before
    public void login() {
        Credentials c = Credentials.getInstance();
        anna.can(BrowseTheWeb.with(herBrowser))
                .can(Authenticate.withCredentials(c.getUsername(), c.getPassword()));
        anna.attemptsTo(openTheLoginPageAndLogin);
    }

    @Test
    public void start_a_conversation_by_sending_a_message() {
        String otherUser = "User";

        givenThat(anna).wasAbleTo(browseToMessagesPage);

        when(anna).attemptsTo(DraftANewMessage.forUser(otherUser).withText("This Is A Test Message"));
        and(anna).wasAbleTo(sendTheMessage);

        then(anna).should(eventually(seeThat(Conversations.includeConversationWith(otherUser))));
    }

    @After
    public void logout() {
        anna.attemptsTo(logOut);
    }
}
