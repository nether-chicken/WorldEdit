/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.function.factory;

import com.google.common.base.Function;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.GroundFunction;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.NoiseFilter2D;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.visitor.LayerVisitor;
import com.sk89q.worldedit.math.noise.RandomNoise;
import com.sk89q.worldedit.regions.Region;

import static com.sk89q.worldedit.regions.Regions.*;

public class Scatter implements OperationFactory {

    private final Function<EditContext, ? extends RegionFunction> regionFunctionFactory;
    private final double density;

    public Scatter(Function<EditContext, ? extends RegionFunction> regionFunctionFactory, double density) {
        this.regionFunctionFactory = regionFunctionFactory;
        this.density = density;
        new NoiseFilter2D(new RandomNoise(), density); // Check validity density argument
    }

    @Override
    public Operation createOperation(EditContext context) {
        Region region = context.getRegion();
        GroundFunction ground = new GroundFunction(new ExistingBlockMask(context.getDestination()), regionFunctionFactory.apply(context));
        LayerVisitor visitor = new LayerVisitor(asFlatRegion(region), minimumBlockY(region), maximumBlockY(region), ground);
        visitor.setMask(new NoiseFilter2D(new RandomNoise(), density));
        return visitor;
    }

    @Override
    public String toString() {
        return "scatter " + regionFunctionFactory;
    }

}