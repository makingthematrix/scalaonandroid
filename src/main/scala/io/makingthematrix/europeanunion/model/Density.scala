/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Based on https://github.com/gluonhq/gluon-samples/tree/master/fiftystates
 */

package io.makingthematrix.europeanunion.model

object Density {
    sealed trait Density extends Comparable[Density] {
        val initial: Int
        val end: Int

        override def compareTo(d: Density): Int = initial - d.initial
    }

    case object D000 extends Density { val initial = 0; val end = 10 }
    case object D010 extends Density { val initial = 10; val end = 50 }
    case object D050 extends Density { val initial = 50; val end = 100 }
    case object D100 extends Density { val initial = 100; val end = 250 }
    case object D250 extends Density { val initial = 250; val end = 500 }
    case object D500 extends Density { val initial = 500; val end = 10000 }

    private val densities = Seq(D000, D010, D050, D100, D250, D500)

    def getDensity(state: Country): Density =
        densities.find(d => d.initial >= state.density && d.end < state.density).getOrElse(D000)
}