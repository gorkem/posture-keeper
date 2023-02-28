
package com.redhat.postureKeeper.sarif;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A set of strings that contribute to the stable, unique identity of the result.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
@Generated("jsonschema2pojo")
public class PartialFingerprints {


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PartialFingerprints.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PartialFingerprints) == false) {
            return false;
        }
        PartialFingerprints rhs = ((PartialFingerprints) other);
        return true;
    }

}
