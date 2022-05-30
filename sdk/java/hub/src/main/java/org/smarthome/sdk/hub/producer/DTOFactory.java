package org.smarthome.sdk.hub.producer;

import org.smarthome.sdk.hub.device.*;

import org.smarthome.sdk.hub.device.constraints.EnumValuesConstraint;
import org.smarthome.sdk.hub.device.constraints.FloatingPointConstraint;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.models.ComponentMetadata;
import org.smarthome.sdk.models.DeviceMetadata;
import org.smarthome.sdk.models.DeviceProperty;
import org.smarthome.sdk.models.constraints.EnumConstraint;
import org.smarthome.sdk.models.constraints.RangeConstraint;

import java.util.Arrays;

/**
 * Contains functions to generate data transfer object
 * @author Al-Sah
 */
public class DTOFactory {


    public static DeviceMetadata getDeviceMetadata(Device device) throws RuntimeException {
        var components = generateComponents(device.getComponents());
        return new DeviceMetadata(device.getId(), device.getType(), device.getName(), components);
    }


    private static DeviceProperty generatePropertyDTO(DevicePropertyBase property){

        Object constraint = null;
        String value = null;
        if(property instanceof ConstantDeviceProperty){
            value = ((ConstantDeviceProperty<?>) property).getValue().toString();
        }
        if(property instanceof WritableDeviceProperty){
            value = ((WritableDeviceProperty<?>) property).getValue().toString();
            constraint = generateConstraintDTO(((WritableDeviceProperty<?>) property).getConstraint());
        }
        return new DeviceProperty(property.getName(), property.getUnit(), property.getDescription(), constraint, value);
    }


    private static Object generateConstraintDTO(PropertyConstraint constraint) throws RuntimeException{

        if(constraint instanceof EnumValuesConstraint){
            var values = ((EnumValuesConstraint<?>) constraint).getValues();
            return new EnumConstraint(
                    constraint.getType(),
                    (String[]) Arrays.stream(values).map(Object::toString).toArray()
            );
        }

        if(constraint instanceof FloatingPointConstraint){
            return new RangeConstraint( constraint.getType(),
                    Double.toString(((FloatingPointConstraint) constraint).getMin()),
                    Double.toString(((FloatingPointConstraint) constraint).getMax())
            );
        }

        if(constraint instanceof IntegerNumberConstraint){
            return new RangeConstraint( constraint.getType(),
                    Long.toString(((IntegerNumberConstraint) constraint).getMin()),
                    Long.toString(((IntegerNumberConstraint) constraint).getMax())
            );
        }
        throw new RuntimeException("Cannot generate constraint dto: " + constraint.getType());
    }

    private static ComponentMetadata[] generateComponents(DeviceComponent[] source){

        var components = new ComponentMetadata[source.length];

        for (int i = 0; i < source.length; i++) {
            DeviceComponent component = source[i];
            components[i] = new ComponentMetadata(
                    component.getId(),
                    component.getName(),
                    component.getDatatype(),
                    component.getMainProperty() == null ? null : generatePropertyDTO(component.getMainProperty()),
                    generatePropertiesDTO(component.getConstProperties()),
                    generatePropertiesDTO(component.getWritableProperties())
            );
        }
        return components;
    }

    private static DeviceProperty[] generatePropertiesDTO(DevicePropertyBase[] properties){
        if(properties == null){
            return null;
        }
        var result = new DeviceProperty[properties.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = generatePropertyDTO(properties[i]);
        }
        return result;
    }
}
