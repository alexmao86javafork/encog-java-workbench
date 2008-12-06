/*
 * Encog Workbench v1.x
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008, Heaton Research Inc., and individual contributors.
 * See the copyright.txt in the distribution for a full listing of 
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.encog.workbench.dialogs.training.anneal;

import java.awt.Frame;

import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.Train;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.workbench.dialogs.training.BasicTrainingProgress;

public class ProgressAnneal extends BasicTrainingProgress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double startTemp;
	private final double endTemp;
	private final int cycles;

	public ProgressAnneal(final Frame owner, final BasicNetwork network,
			final NeuralDataSet trainingData, final double maxError,
			final double startTemp, final double endTemp, final int cycles) {
		super(owner);
		setTitle("Simulated Annealing Training");
		setNetwork(network);
		setTrainingData(trainingData);
		this.cycles = cycles;
		this.startTemp = startTemp;
		this.endTemp = endTemp;
		setMaxError(maxError);

	}

	@Override
	public void iteration() {

		getTrain().iteration();

	}

	@Override
	public void shutdown() {
	}

	@Override
	public void startup() {
		final Train train = new NeuralSimulatedAnnealing(getNetwork(),
				getTrainingData(), this.startTemp, this.endTemp, this.cycles);

		setTrain(train);
	}

}