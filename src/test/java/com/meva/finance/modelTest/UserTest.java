//package com.meva.finance.modelTest;
//
//import com.meva.finance.model.Family;
//import com.meva.finance.model.User;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(MockitoExtension.class)
//
//public class UserTest {
//    @Mock
//    private User user;
//    @Mock
//    private Family family;
//
//    @BeforeEach
//    public void before() {
//        startUser();
//    }
//
//    @Test
//    public void testUser() {
//        User user2 = new User("12345678998", "Gilson", "M",
//                LocalDate.of(2000, 10, 10),
//                "Rio de janeiro", "Nova Iguaçu", family);
//
//        assertEquals(user.getCpf(), user2.getCpf());
//    }
//
//    private void startUser() {
//        family = new Family();
//        user = new User("12345678998", "Gilson", "M",
//                LocalDate.of(2000, 10, 10),
//                "Rio de janeiro", "Nova Iguaçu", family);
//
//    }
//}
