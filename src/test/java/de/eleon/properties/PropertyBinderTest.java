package de.eleon.properties;


import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static de.eleon.properties.PropertyBinder.from;
import static de.eleon.properties.PropertyBinder.property;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PropertyBinderTest {

    @Test
    public void shouldSetStringValue() throws Exception {

        Property<UseMe, String> stringProperty = property(from(UseMe.class).getName());

        UseMe test = new UseMe();

        stringProperty.set(test, "Dominik");
        assertThat(test.getName(), is("Dominik"));

        test.setName("Michael");
        assertThat(stringProperty.get(test), is("Michael"));
    }


    @Test
    public void shouldSetIntegerValue() throws Exception {

        Property<UseMe, Integer> integerProperty = property(from(UseMe.class).getChildren());

        UseMe test = new UseMe();

        integerProperty.set(test, 10);
        assertThat(test.getChildren(), is(10));

        test.setChildren(20);
        assertThat(integerProperty.get(test), is(20));
    }

    @Test
    public void shouldSetDateValue() throws Exception {

        Property<UseMe, Date> dateProperty = property(from(UseMe.class).getBirthDate());

        UseMe test = new UseMe();

        final Date testDate = DateTime.parse("1910-01-01").toDate();
        dateProperty.set(test, testDate);
        assertThat(test.getBirthDate(), is(testDate));

        final Date testDate2 = DateTime.parse("1920-02-02").toDate();
        test.setBirthDate(testDate2);
        assertThat(dateProperty.get(test), is(testDate2));
    }

    @Test
    public void shouldSetBooleanValue() throws Exception {

        Property<UseMe, Boolean> booleanProperty = property(from(UseMe.class).isActive());

        UseMe test = new UseMe();

        booleanProperty.set(test, true);
        assertThat(test.isActive(), is(true));

        test.setActive(false);
        assertThat(booleanProperty.get(test), is(false));
    }

    public static class UseMe {

        private String name;
        private int children;
        private Date birthDate;
        private boolean active;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getChildren() {
            return children;
        }

        public void setChildren(int children) {
            this.children = children;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

}