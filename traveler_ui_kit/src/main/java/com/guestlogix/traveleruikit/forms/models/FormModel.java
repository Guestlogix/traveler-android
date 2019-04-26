package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.FormFieldType;

/**
 * Describes a field in the form.
 * Must use one of the available child classes.
 * <p>
 * See:
 * <ul>
 * <li>{@link ButtonFormModel}</li>
 * <li>{@link DateFormModel}</li>
 * <li>{@link HeaderFormModel}</li>
 * <li>{@link MessageFormModel}</li>
 * <li>{@link QuantityFormModel}</li>
 * <li>{@link SpinnerFormModel}</li>
 * <li>{@link TextFormModel}</li>
 * </ul>
 */
public abstract class FormModel {
    /*
        Future Considerations:
            - More model types are required to have a better user experience. You could add types such as
                - Email
                - Phone
                - Range of numbers/dates

            - The form library could also include validators. This will improve performance since the data would not need
              to be propagated back to whatever uses the activity.

           - There should be a boolean isRequired in this class. So we can display an asterisk next to important fields.
     */

    public abstract FormFieldType getType();
}
