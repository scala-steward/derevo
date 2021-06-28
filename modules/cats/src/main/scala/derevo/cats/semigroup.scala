package derevo.cats

import cats.Semigroup
import magnolia.{CaseClass, Magnolia, SealedTrait}
import derevo.Derivation
import scala.annotation.implicitNotFound
import derevo.NewTypeDerivation

object semigroup extends Derivation[Semigroup] with NewTypeDerivation[Semigroup] {
  type Typeclass[T] = Semigroup[T]

  def combine[T](ctx: CaseClass[Semigroup, T]): Semigroup[T] = new Semigroup[T] {
    override def combine(x: T, y: T): T =
      ctx.construct(param => param.typeclass.combine(param.dereference(x), param.dereference(y)))
  }

  def dispatch[T](ctx: SealedTrait[Semigroup, T])(implicit absurd: SemigroupSumAbsurd): Semigroup[T] = absurd.sg
  implicit def instance[T]: Semigroup[T] = macro Magnolia.gen[T]
}

@implicitNotFound("Can not derive Semigroups for sealed families")
abstract class SemigroupSumAbsurd {
  def sg[A]: Semigroup[A]
}
